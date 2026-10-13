package com.freightfox.transporterassignment.controller;

import com.freightfox.transporterassignment.dto.ApiResponse;
import com.freightfox.transporterassignment.dto.AssignmentRequest;
import com.freightfox.transporterassignment.dto.AssignmentResponse;
import com.freightfox.transporterassignment.dto.TransporterInputRequest;
import com.freightfox.transporterassignment.service.TransporterOptimizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transporters")
@RequiredArgsConstructor
@Tag(name = "Transporter Assignment", description = "Endpoints for freight lane input and optimized transporter allocation")
public class TransporterController {

    private final TransporterOptimizationService transporterOptimizationService;

    @PostMapping("/input")
    @Operation(summary = "Submit Input Data", description = "Stores and upserts lanes and transporter quotes into the database")
    public ResponseEntity<ApiResponse> submitInputData(@Valid @RequestBody TransporterInputRequest request) {
        ApiResponse response = transporterOptimizationService.saveInputData(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/assignment")
    @Operation(summary = "Get Optimized Results", description = "Computes optimal transporter-to-lane assignment minimizing total cost with full lane coverage")
    public ResponseEntity<AssignmentResponse> getOptimizedResults(@Valid @RequestBody AssignmentRequest request) {
        AssignmentResponse response = transporterOptimizationService.getOptimizedAssignment(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

