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
@Schema(description = "Generic API response status wrapper")
public class ApiResponse {

    @Schema(description = "Status of the operation", example = "success")
    private String status;

    @Schema(description = "User-facing message", example = "Input data saved successfully.")
    private String message;
}
