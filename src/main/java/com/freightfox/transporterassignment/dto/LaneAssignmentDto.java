package com.freightfox.transporterassignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Assignment of a lane to an awarded transporter")
public class LaneAssignmentDto {

    @Schema(description = "ID of the lane", example = "1")
    private Long laneId;

    @Schema(description = "ID of the assigned transporter", example = "4")
    private Long transporterId;
}
