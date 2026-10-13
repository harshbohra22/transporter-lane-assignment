package com.freightfox.transporterassignment.strategy;

import com.freightfox.transporterassignment.dto.LaneAssignmentDto;
import com.freightfox.transporterassignment.exception.InfeasibleAssignmentException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class GreedyOptimizationStrategy implements OptimizationStrategy {

    @Override
    public OptimizationResult optimize(Set<Long> laneIds, List<Long> transporterIds, Map<Long, Map<Long, Double>> quotesMatrix, int maxTransporters) {
        if (laneIds.isEmpty()) {
            return OptimizationResult.builder()
                    .totalCost(0.0)
                    .assignments(Collections.emptyList())
                    .selectedTransporters(Collections.emptyList())
                    .build();
        }

        Set<Long> selectedTransporters = new LinkedHashSet<>();
        Set<Long> uncoveredLanes = new HashSet<>(laneIds);

        for (Long laneId : laneIds) {
            List<Long> quoters = new ArrayList<>();
            for (Long tId : transporterIds) {
                Map<Long, Double> q = quotesMatrix.get(tId);
                if (q != null && q.containsKey(laneId)) {
                    quoters.add(tId);
                }
            }
            if (quoters.isEmpty()) {
                throw new InfeasibleAssignmentException("Lane with ID " + laneId + " has no available transporter quotes");
            }
            if (quoters.size() == 1) {
                selectedTransporters.add(quoters.get(0));
                uncoveredLanes.remove(laneId);
            }
        }

        for (Long tId : selectedTransporters) {
            Map<Long, Double> q = quotesMatrix.get(tId);
            if (q != null) {
                uncoveredLanes.removeAll(q.keySet());
            }
        }

        while (!uncoveredLanes.isEmpty()) {
            if (selectedTransporters.size() >= maxTransporters) {
                throw new InfeasibleAssignmentException("Cannot achieve full lane coverage with at most " + maxTransporters + " transporters");
            }

            Long bestTransporter = null;
            int maxNewlyCovered = 0;
            double lowestAvgCost = Double.MAX_VALUE;

            for (Long tId : transporterIds) {
                if (selectedTransporters.contains(tId)) {
                    continue;
                }
                Map<Long, Double> quotes = quotesMatrix.get(tId);
                if (quotes == null) {
                    continue;
                }

                int newlyCovered = 0;
                double costSum = 0.0;
                for (Long laneId : uncoveredLanes) {
                    if (quotes.containsKey(laneId)) {
                        newlyCovered++;
                        costSum += quotes.get(laneId);
                    }
                }

                if (newlyCovered > 0) {
                    double avgCost = costSum / newlyCovered;
                    if (newlyCovered > maxNewlyCovered || (newlyCovered == maxNewlyCovered && avgCost < lowestAvgCost)) {
                        maxNewlyCovered = newlyCovered;
                        lowestAvgCost = avgCost;
                        bestTransporter = tId;
                    }
                }
            }

            if (bestTransporter == null) {
                throw new InfeasibleAssignmentException("Unable to cover remaining lanes with available transporters");
            }

            selectedTransporters.add(bestTransporter);
            Map<Long, Double> q = quotesMatrix.get(bestTransporter);
            if (q != null) {
                uncoveredLanes.removeAll(q.keySet());
            }
        }

        while (selectedTransporters.size() < maxTransporters && selectedTransporters.size() < transporterIds.size()) {
            Long bestAdditional = null;
            double maxSavings = 0.0;

            for (Long tId : transporterIds) {
                if (selectedTransporters.contains(tId)) {
                    continue;
                }
                Map<Long, Double> candidateQuotes = quotesMatrix.get(tId);
                if (candidateQuotes == null) {
                    continue;
                }

                double savings = 0.0;
                for (Long laneId : laneIds) {
                    if (candidateQuotes.containsKey(laneId)) {
                        double currentBest = getBestQuoteForLane(laneId, selectedTransporters, quotesMatrix);
                        double candidateQuote = candidateQuotes.get(laneId);
                        if (candidateQuote < currentBest) {
                            savings += (currentBest - candidateQuote);
                        }
                    }
                }

                if (savings > maxSavings) {
                    maxSavings = savings;
                    bestAdditional = tId;
                }
            }

            if (bestAdditional != null && maxSavings > 0) {
                selectedTransporters.add(bestAdditional);
            } else {
                break;
            }
        }

        double totalCost = 0.0;
        List<LaneAssignmentDto> assignments = new ArrayList<>();
        List<Long> sortedLanes = new ArrayList<>(laneIds);
        Collections.sort(sortedLanes);

        for (Long laneId : sortedLanes) {
            Long assignedTransporter = null;
            double minQuote = Double.MAX_VALUE;

            for (Long tId : selectedTransporters) {
                Map<Long, Double> quotes = quotesMatrix.get(tId);
                if (quotes != null && quotes.containsKey(laneId)) {
                    double q = quotes.get(laneId);
                    if (q < minQuote) {
                        minQuote = q;
                        assignedTransporter = tId;
                    }
                }
            }

            totalCost += minQuote;
            assignments.add(new LaneAssignmentDto(laneId, assignedTransporter));
        }

        List<Long> selectedList = new ArrayList<>(selectedTransporters);
        Collections.sort(selectedList);

        return OptimizationResult.builder()
                .totalCost(totalCost)
                .assignments(assignments)
                .selectedTransporters(selectedList)
                .build();
    }

    private double getBestQuoteForLane(Long laneId, Set<Long> currentTransporters, Map<Long, Map<Long, Double>> quotesMatrix) {
        double minQuote = Double.MAX_VALUE;
        for (Long tId : currentTransporters) {
            Map<Long, Double> q = quotesMatrix.get(tId);
            if (q != null && q.containsKey(laneId)) {
                minQuote = Math.min(minQuote, q.get(laneId));
            }
        }
        return minQuote;
    }
}

