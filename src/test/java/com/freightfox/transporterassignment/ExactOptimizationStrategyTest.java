package com.freightfox.transporterassignment;

import com.freightfox.transporterassignment.exception.InfeasibleAssignmentException;
import com.freightfox.transporterassignment.strategy.ExactOptimizationStrategy;
import com.freightfox.transporterassignment.strategy.OptimizationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExactOptimizationStrategyTest {

    private ExactOptimizationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ExactOptimizationStrategy();
    }

    @Test
    void testOptimizationTestCase1() {
        Set<Long> laneIds = new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        List<Long> transporterIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L);

        Map<Long, Map<Long, Double>> quotesMatrix = new HashMap<>();

        Map<Long, Double> t1 = new HashMap<>();
        t1.put(1L, 20835.0); t1.put(2L, 10512.0); t1.put(3L, 22105.0); t1.put(4L, 42481.0); t1.put(5L, 19862.0);
        quotesMatrix.put(1L, t1);

        Map<Long, Double> t2 = new HashMap<>();
        t2.put(1L, 48844.0); t2.put(2L, 31326.0); t2.put(3L, 18640.0); t2.put(4L, 45828.0); t2.put(5L, 18297.0);
        quotesMatrix.put(2L, t2);

        Map<Long, Double> t3 = new HashMap<>();
        t3.put(1L, 39020.0); t3.put(2L, 20648.0); t3.put(3L, 31438.0); t3.put(4L, 36447.0); t3.put(5L, 12789.0);
        quotesMatrix.put(3L, t3);

        Map<Long, Double> t4 = new HashMap<>();
        t4.put(1L, 14400.0); t4.put(2L, 44514.0); t4.put(3L, 14316.0); t4.put(4L, 10678.0); t4.put(5L, 13032.0);
        quotesMatrix.put(4L, t4);

        Map<Long, Double> t5 = new HashMap<>();
        t5.put(1L, 11601.0); t5.put(2L, 19760.0); t5.put(3L, 40870.0); t5.put(4L, 20635.0); t5.put(5L, 26421.0);
        quotesMatrix.put(5L, t5);

        Map<Long, Double> t6 = new HashMap<>();
        t6.put(1L, 35095.0); t6.put(2L, 12494.0); t6.put(3L, 17808.0); t6.put(4L, 36210.0); t6.put(5L, 39444.0);
        quotesMatrix.put(6L, t6);

        Map<Long, Double> t7 = new HashMap<>();
        t7.put(1L, 26070.0); t7.put(2L, 41098.0); t7.put(3L, 20932.0); t7.put(4L, 16897.0); t7.put(5L, 27938.0);
        quotesMatrix.put(7L, t7);

        OptimizationResult result = strategy.optimize(laneIds, transporterIds, quotesMatrix, 3);

        assertNotNull(result);
        assertEquals(5, result.getAssignments().size());
        assertTrue(result.getSelectedTransporters().size() <= 3);
        assertTrue(result.getTotalCost() > 0);
    }

    @Test
    void testOptimizationTestCase2() {
        Set<Long> laneIds = new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        List<Long> transporterIds = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, Map<Long, Double>> quotesMatrix = new HashMap<>();

        Map<Long, Double> t1 = new HashMap<>();
        t1.put(1L, 1000.0); t1.put(2L, 1500.0);
        quotesMatrix.put(1L, t1);

        Map<Long, Double> t2 = new HashMap<>();
        t2.put(3L, 2000.0); t2.put(4L, 1800.0);
        quotesMatrix.put(2L, t2);

        Map<Long, Double> t3 = new HashMap<>();
        t3.put(5L, 1200.0);
        quotesMatrix.put(3L, t3);

        Map<Long, Double> t4 = new HashMap<>();
        t4.put(1L, 1100.0); t4.put(4L, 1750.0);
        quotesMatrix.put(4L, t4);

        OptimizationResult result = strategy.optimize(laneIds, transporterIds, quotesMatrix, 3);

        assertNotNull(result);
        assertEquals(7500.0, result.getTotalCost(), 0.001);
        assertEquals(5, result.getAssignments().size());
        assertEquals(3, result.getSelectedTransporters().size());
        assertTrue(result.getSelectedTransporters().containsAll(Arrays.asList(1L, 2L, 3L)));
    }

    @Test
    void testInfeasibleWhenTransporterLimitIsTooLow() {
        Set<Long> laneIds = new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        List<Long> transporterIds = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, Map<Long, Double>> quotesMatrix = new HashMap<>();

        Map<Long, Double> t1 = new HashMap<>();
        t1.put(1L, 1000.0); t1.put(2L, 1500.0);
        quotesMatrix.put(1L, t1);

        Map<Long, Double> t2 = new HashMap<>();
        t2.put(3L, 2000.0); t2.put(4L, 1800.0);
        quotesMatrix.put(2L, t2);

        Map<Long, Double> t3 = new HashMap<>();
        t3.put(5L, 1200.0);
        quotesMatrix.put(3L, t3);

        Map<Long, Double> t4 = new HashMap<>();
        t4.put(1L, 1100.0); t4.put(4L, 1750.0);
        quotesMatrix.put(4L, t4);

        assertThrows(InfeasibleAssignmentException.class, () -> {
            strategy.optimize(laneIds, transporterIds, quotesMatrix, 2);
        });
    }

    @Test
    void testUncoveredLaneThrowsException() {
        Set<Long> laneIds = new HashSet<>(Arrays.asList(1L, 2L, 99L));
        List<Long> transporterIds = Arrays.asList(1L);

        Map<Long, Map<Long, Double>> quotesMatrix = new HashMap<>();
        Map<Long, Double> t1 = new HashMap<>();
        t1.put(1L, 1000.0);
        quotesMatrix.put(1L, t1);

        assertThrows(InfeasibleAssignmentException.class, () -> {
            strategy.optimize(laneIds, transporterIds, quotesMatrix, 2);
        });
    }
}

