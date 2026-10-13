package com.freightfox.transporterassignment.strategy;

import com.freightfox.transporterassignment.dto.LaneAssignmentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptimizationResult {
    private Double totalCost;
    @Builder.Default
    private List<LaneAssignmentDto> assignments = new ArrayList<>();
    @Builder.Default
    private List<Long> selectedTransporters = new ArrayList<>();
}
