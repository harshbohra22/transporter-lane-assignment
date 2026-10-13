package com.freightfox.transporterassignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Optimized transporter assignment result")
public class AssignmentResponse {

    @Schema(description = "Status of the optimization response", example = "success")
    @Builder.Default
    private String status = "success";

    @Schema(description = "Total minimized procurement cost across all lanes", example = "75000")
    private Double totalCost;

    @Schema(description = "List of lane-to-transporter assignments ensuring full lane coverage")
    @Builder.Default
    private List<LaneAssignmentDto> assignments = new ArrayList<>();

    @Schema(description = "List of distinct transporter IDs awarded lanes", example = "[4, 6, 3]")
    @Builder.Default
    private List<Long> selectedTransporters = new ArrayList<>();
}
