package com.freightfox.transporterassignment.strategy;

import com.freightfox.transporterassignment.dto.LaneAssignmentDto;
import com.freightfox.transporterassignment.exception.InfeasibleAssignmentException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ExactOptimizationStrategy implements OptimizationStrategy {

    @Override
    public OptimizationResult optimize(Set<Long> laneIds, List<Long> transporterIds, Map<Long, Map<Long, Double>> quotesMatrix, int maxTransporters) {
        if (laneIds.isEmpty()) {
            return OptimizationResult.builder()
                    .totalCost(0.0)
                    .assignments(Collections.emptyList())
                    .selectedTransporters(Collections.emptyList())
                    .build();
        }

        for (Long laneId : laneIds) {
            boolean hasQuote = false;
            for (Long tId : transporterIds) {
                Map<Long, Double> quotes = quotesMatrix.get(tId);
                if (quotes != null && quotes.containsKey(laneId)) {
                    hasQuote = true;
                    break;
                }
            }
            if (!hasQuote) {
                throw new InfeasibleAssignmentException("Lane with ID " + laneId + " has no available transporter quotes");
            }
        }

        Set<Long> mandatoryTransporters = new HashSet<>();
        for (Long laneId : laneIds) {
            List<Long> quotingTransporters = new ArrayList<>();
            for (Long tId : transporterIds) {
                Map<Long, Double> quotes = quotesMatrix.get(tId);
                if (quotes != null && quotes.containsKey(laneId)) {
                    quotingTransporters.add(tId);
                }
            }
            if (quotingTransporters.size() == 1) {
                mandatoryTransporters.add(quotingTransporters.get(0));
            }
        }

        if (mandatoryTransporters.size() > maxTransporters) {
            throw new InfeasibleAssignmentException("At least " + mandatoryTransporters.size() + " transporters are required to cover all lanes, which exceeds maxTransporters (" + maxTransporters + ")");
        }

        List<Long> candidateTransporters = new ArrayList<>();
        for (Long tId : transporterIds) {
            if (!mandatoryTransporters.contains(tId)) {
                candidateTransporters.add(tId);
            }
        }

        int targetSize = Math.min(maxTransporters, transporterIds.size());
        OptimizationResult bestResult = null;

        for (int k = mandatoryTransporters.size(); k <= targetSize; k++) {
            int additionalNeeded = k - mandatoryTransporters.size();
            List<List<Long>> combinations = new ArrayList<>();
            generateCombinations(candidateTransporters, 0, additionalNeeded, new ArrayList<>(), combinations);

            for (List<Long> additional : combinations) {
                Set<Long> subset = new HashSet<>(mandatoryTransporters);
                subset.addAll(additional);

                OptimizationResult currentResult = evaluateSubset(subset, laneIds, quotesMatrix);
                if (currentResult != null) {
                    if (bestResult == null || isBetter(currentResult, bestResult)) {
                        bestResult = currentResult;
                    }
                }
            }
        }

        if (bestResult == null) {
            throw new InfeasibleAssignmentException("Cannot achieve full lane coverage with at most " + maxTransporters + " transporters");
        }

        return bestResult;
    }

    private void generateCombinations(List<Long> list, int start, int k, List<Long> current, List<List<Long>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < list.size(); i++) {
            current.add(list.get(i));
            generateCombinations(list, i + 1, k, current, result);
            current.remove(current.size() - 1);
        }
    }

    private OptimizationResult evaluateSubset(Set<Long> subset, Set<Long> laneIds, Map<Long, Map<Long, Double>> quotesMatrix) {
        double totalCost = 0.0;
        List<LaneAssignmentDto> assignments = new ArrayList<>();
        Set<Long> actuallyUsedTransporters = new LinkedHashSet<>();

        List<Long> sortedLaneIds = new ArrayList<>(laneIds);
        Collections.sort(sortedLaneIds);

        for (Long laneId : sortedLaneIds) {
            Long bestTransporter = null;
            Double minQuote = Double.MAX_VALUE;

            for (Long tId : subset) {
                Map<Long, Double> quotes = quotesMatrix.get(tId);
                if (quotes != null && quotes.containsKey(laneId)) {
                    Double quote = quotes.get(laneId);
                    if (quote < minQuote) {
                        minQuote = quote;
                        bestTransporter = tId;
                    }
                }
            }

            if (bestTransporter == null) {
                return null;
            }

            totalCost += minQuote;
            assignments.add(new LaneAssignmentDto(laneId, bestTransporter));
            actuallyUsedTransporters.add(bestTransporter);
        }

        List<Long> selectedList = new ArrayList<>(subset);
        Collections.sort(selectedList);

        return OptimizationResult.builder()
                .totalCost(totalCost)
                .assignments(assignments)
                .selectedTransporters(selectedList)
                .build();
    }

    private boolean isBetter(OptimizationResult current, OptimizationResult best) {
        double diff = current.getTotalCost() - best.getTotalCost();
        if (Math.abs(diff) > 1e-4) {
            return diff < 0;
        }
        return current.getSelectedTransporters().size() > best.getSelectedTransporters().size();
    }
}

