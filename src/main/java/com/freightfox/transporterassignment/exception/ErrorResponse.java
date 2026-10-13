package com.freightfox.transporterassignment.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Structured API Error Response")
public class ErrorResponse {

    @Schema(description = "Status indicator", example = "error")
    @Builder.Default
    private String status = "error";

    @Schema(description = "Human-readable error explanation", example = "Constraint violation or validation failure")
    private String message;

    @Schema(description = "HTTP Status Code", example = "400")
    private int statusCode;

    @Schema(description = "Timestamp of the error")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @Schema(description = "Field-level validation error details, if any")
    private List<String> details;
}
