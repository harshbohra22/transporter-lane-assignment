package com.freightfox.transporterassignment.service;

import com.freightfox.transporterassignment.dto.ApiResponse;
import com.freightfox.transporterassignment.dto.AssignmentRequest;
import com.freightfox.transporterassignment.dto.AssignmentResponse;
import com.freightfox.transporterassignment.dto.LaneInputDto;
import com.freightfox.transporterassignment.dto.LaneQuoteInputDto;
import com.freightfox.transporterassignment.dto.TransporterInputDto;
import com.freightfox.transporterassignment.dto.TransporterInputRequest;
import com.freightfox.transporterassignment.entity.Lane;
import com.freightfox.transporterassignment.entity.LaneQuote;
import com.freightfox.transporterassignment.entity.Transporter;
import com.freightfox.transporterassignment.exception.InvalidInputException;
import com.freightfox.transporterassignment.repository.LaneQuoteRepository;
import com.freightfox.transporterassignment.repository.LaneRepository;
import com.freightfox.transporterassignment.repository.TransporterRepository;
import com.freightfox.transporterassignment.strategy.ExactOptimizationStrategy;
import com.freightfox.transporterassignment.strategy.GreedyOptimizationStrategy;
import com.freightfox.transporterassignment.strategy.OptimizationResult;
import com.freightfox.transporterassignment.strategy.OptimizationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransporterOptimizationService {

    private final LaneRepository laneRepository;
    private final TransporterRepository transporterRepository;
    private final LaneQuoteRepository laneQuoteRepository;
    private final ExactOptimizationStrategy exactOptimizationStrategy;
    private final GreedyOptimizationStrategy greedyOptimizationStrategy;

    @Transactional
    public ApiResponse saveInputData(TransporterInputRequest request) {
        if (request.getLanes() == null || request.getLanes().isEmpty()) {
            throw new InvalidInputException("Lanes list cannot be empty");
        }
        if (request.getTransporters() == null || request.getTransporters().isEmpty()) {
            throw new InvalidInputException("Transporters list cannot be empty");
        }

        Map<Long, Lane> laneMap = new HashMap<>();
        for (LaneInputDto lDto : request.getLanes()) {
            Lane lane = laneRepository.findById(lDto.getId())
                    .orElse(Lane.builder().id(lDto.getId()).build());
            lane.setOrigin(lDto.getOrigin());
            lane.setDestination(lDto.getDestination());
            laneMap.put(lDto.getId(), lane);
        }
        laneRepository.saveAll(laneMap.values());

        for (TransporterInputDto tDto : request.getTransporters()) {
            Transporter transporter = transporterRepository.findById(tDto.getId())
                    .orElse(Transporter.builder().id(tDto.getId()).build());
            transporter.setName(tDto.getName());
            transporterRepository.save(transporter);

            laneQuoteRepository.deleteByTransporterId(transporter.getId());
            transporter.clearQuotes();

            if (tDto.getLaneQuotes() != null) {
                for (LaneQuoteInputDto qDto : tDto.getLaneQuotes()) {
                    Lane targetLane = laneMap.get(qDto.getLaneId());
                    if (targetLane == null) {
                        targetLane = laneRepository.findById(qDto.getLaneId())
                                .orElseThrow(() -> new InvalidInputException("Lane with ID " + qDto.getLaneId() + " does not exist"));
                    }
                    LaneQuote quote = LaneQuote.builder()
                            .transporter(transporter)
                            .lane(targetLane)
                            .quote(qDto.getQuote())
                            .build();
                    transporter.addQuote(quote);
                }
            }
            transporterRepository.save(transporter);
        }

        return ApiResponse.builder()
                .status("success")
                .message("Input data saved successfully.")
                .build();
    }

    @Transactional(readOnly = true)
    public AssignmentResponse getOptimizedAssignment(AssignmentRequest request) {
        if (request.getMaxTransporters() == null || request.getMaxTransporters() < 1) {
            throw new InvalidInputException("maxTransporters must be at least 1");
        }

        List<Lane> allLanes = laneRepository.findAll();
        if (allLanes.isEmpty()) {
            throw new InvalidInputException("No lanes found in the system. Please submit input data first.");
        }

        List<Transporter> allTransporters = transporterRepository.findAll();
        if (allTransporters.isEmpty()) {
            throw new InvalidInputException("No transporters found in the system. Please submit input data first.");
        }

        Set<Long> laneIds = allLanes.stream().map(Lane::getId).collect(Collectors.toSet());
        List<Long> transporterIds = allTransporters.stream().map(Transporter::getId).collect(Collectors.toList());

        Map<Long, Map<Long, Double>> quotesMatrix = new HashMap<>();
        for (Transporter t : allTransporters) {
            Map<Long, Double> quotes = new HashMap<>();
            if (t.getLaneQuotes() != null) {
                for (LaneQuote lq : t.getLaneQuotes()) {
                    quotes.put(lq.getLane().getId(), lq.getQuote());
                }
            }
            quotesMatrix.put(t.getId(), quotes);
        }

        OptimizationStrategy strategy = selectStrategy(transporterIds.size(), request.getMaxTransporters());
        OptimizationResult result = strategy.optimize(laneIds, transporterIds, quotesMatrix, request.getMaxTransporters());

        return AssignmentResponse.builder()
                .status("success")
                .totalCost(result.getTotalCost())
                .assignments(result.getAssignments())
                .selectedTransporters(result.getSelectedTransporters())
                .build();
    }

    private OptimizationStrategy selectStrategy(int transporterCount, int maxTransporters) {
        if (transporterCount <= 22 || maxTransporters <= 6) {
            return exactOptimizationStrategy;
        }
        return greedyOptimizationStrategy;
    }
}

