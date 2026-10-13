package com.freightfox.transporterassignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Transporter details and their lane quotes")
public class TransporterInputDto {

    @NotNull(message = "Transporter id is required")
    @Schema(description = "Unique identifier of the transporter", example = "1")
    private Long id;

    @NotBlank(message = "Transporter name cannot be blank")
    @Schema(description = "Name of the transporter", example = "Transporter T1")
    private String name;

    @Valid
    @Builder.Default
    @Schema(description = "List of quotes provided by this transporter")
    private List<LaneQuoteInputDto> laneQuotes = new ArrayList<>();
}
