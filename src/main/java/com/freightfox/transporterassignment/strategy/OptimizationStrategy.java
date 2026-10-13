package com.freightfox.transporterassignment.strategy;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OptimizationStrategy {
    OptimizationResult optimize(Set<Long> laneIds, List<Long> transporterIds, Map<Long, Map<Long, Double>> quotesMatrix, int maxTransporters);
}

