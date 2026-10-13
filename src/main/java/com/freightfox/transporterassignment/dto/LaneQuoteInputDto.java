package com.freightfox.transporterassignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Transporter quote for a specific lane")
public class LaneQuoteInputDto {

    @NotNull(message = "Lane id is required")
    @Schema(description = "ID of the lane being quoted", example = "1")
    private Long laneId;

    @NotNull(message = "Quote amount is required")
    @DecimalMin(value = "0.0", message = "Quote amount cannot be negative")
    @Schema(description = "Quote amount submitted by transporter", example = "5000")
    private Double quote;
}
