package com.freightfox.transporterassignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
@Schema(description = "Bulk input request containing lanes and transporter quotes")
public class TransporterInputRequest {

    @NotEmpty(message = "Lanes list cannot be empty")
    @Valid
    @Builder.Default
    @Schema(description = "List of lanes to be covered")
    private List<LaneInputDto> lanes = new ArrayList<>();

    @NotEmpty(message = "Transporters list cannot be empty")
    @Valid
    @Builder.Default
    @Schema(description = "List of transporters with their quotes")
    private List<TransporterInputDto> transporters = new ArrayList<>();
}
