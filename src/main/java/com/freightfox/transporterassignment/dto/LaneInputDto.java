package com.freightfox.transporterassignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Lane details input DTO")
public class LaneInputDto {

    @NotNull(message = "Lane id is required")
    @Schema(description = "Unique identifier of the lane", example = "1")
    private Long id;

    @NotBlank(message = "Origin cannot be blank")
    @Schema(description = "Origin location", example = "Mumbai")
    private String origin;

    @NotBlank(message = "Destination cannot be blank")
    @Schema(description = "Destination location", example = "Delhi")
    private String destination;
}
