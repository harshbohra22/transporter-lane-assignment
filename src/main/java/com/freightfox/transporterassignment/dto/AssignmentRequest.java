package com.freightfox.transporterassignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to calculate optimal transporter assignment")
public class AssignmentRequest {

    @NotNull(message = "maxTransporters is required")
    @Min(value = 1, message = "maxTransporters must be at least 1")
    @Schema(description = "Maximum number of distinct transporters permitted for lane coverage", example = "3")
    private Integer maxTransporters;
}
