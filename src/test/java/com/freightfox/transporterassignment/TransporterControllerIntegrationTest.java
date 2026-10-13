package com.freightfox.transporterassignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freightfox.transporterassignment.dto.AssignmentRequest;
import com.freightfox.transporterassignment.dto.LaneInputDto;
import com.freightfox.transporterassignment.dto.LaneQuoteInputDto;
import com.freightfox.transporterassignment.dto.TransporterInputDto;
import com.freightfox.transporterassignment.dto.TransporterInputRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransporterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFullWorkflowTestCase1() throws Exception {
        List<LaneInputDto> lanes = Arrays.asList(
                new LaneInputDto(1L, "Mumbai", "Delhi"),
                new LaneInputDto(2L, "Delhi", "Bangalore"),
                new LaneInputDto(3L, "Chennai", "Kolkata"),
                new LaneInputDto(4L, "Pune", "Hyderabad"),
                new LaneInputDto(5L, "Ahmedabad", "Jaipur")
        );

        List<TransporterInputDto> transporters = Arrays.asList(
                new TransporterInputDto(1L, "Transporter T1", Arrays.asList(
                        new LaneQuoteInputDto(1L, 20835.0),
                        new LaneQuoteInputDto(2L, 10512.0),
                        new LaneQuoteInputDto(3L, 22105.0),
                        new LaneQuoteInputDto(4L, 42481.0),
                        new LaneQuoteInputDto(5L, 19862.0)
                )),
                new TransporterInputDto(2L, "Transporter T2", Arrays.asList(
                        new LaneQuoteInputDto(1L, 48844.0),
                        new LaneQuoteInputDto(2L, 31326.0),
                        new LaneQuoteInputDto(3L, 18640.0),
                        new LaneQuoteInputDto(4L, 45828.0),
                        new LaneQuoteInputDto(5L, 18297.0)
                )),
                new TransporterInputDto(3L, "Transporter T3", Arrays.asList(
                        new LaneQuoteInputDto(1L, 39020.0),
                        new LaneQuoteInputDto(2L, 20648.0),
                        new LaneQuoteInputDto(3L, 31438.0),
                        new LaneQuoteInputDto(4L, 36447.0),
                        new LaneQuoteInputDto(5L, 12789.0)
                )),
                new TransporterInputDto(4L, "Transporter T4", Arrays.asList(
                        new LaneQuoteInputDto(1L, 14400.0),
                        new LaneQuoteInputDto(2L, 44514.0),
                        new LaneQuoteInputDto(3L, 14316.0),
                        new LaneQuoteInputDto(4L, 10678.0),
                        new LaneQuoteInputDto(5L, 13032.0)
                )),
                new TransporterInputDto(5L, "Transporter T5", Arrays.asList(
                        new LaneQuoteInputDto(1L, 11601.0),
                        new LaneQuoteInputDto(2L, 19760.0),
                        new LaneQuoteInputDto(3L, 40870.0),
                        new LaneQuoteInputDto(4L, 20635.0),
                        new LaneQuoteInputDto(5L, 26421.0)
                )),
                new TransporterInputDto(6L, "Transporter T6", Arrays.asList(
                        new LaneQuoteInputDto(1L, 35095.0),
                        new LaneQuoteInputDto(2L, 12494.0),
                        new LaneQuoteInputDto(3L, 17808.0),
                        new LaneQuoteInputDto(4L, 36210.0),
                        new LaneQuoteInputDto(5L, 39444.0)
                )),
                new TransporterInputDto(7L, "Transporter T7", Arrays.asList(
                        new LaneQuoteInputDto(1L, 26070.0),
                        new LaneQuoteInputDto(2L, 41098.0),
                        new LaneQuoteInputDto(3L, 20932.0),
                        new LaneQuoteInputDto(4L, 16897.0),
                        new LaneQuoteInputDto(5L, 27938.0)
                ))
        );

        TransporterInputRequest inputRequest = new TransporterInputRequest(lanes, transporters);

        mockMvc.perform(post("/api/v1/transporters/input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Input data saved successfully.")));

        AssignmentRequest assignmentRequest = new AssignmentRequest(3);

        mockMvc.perform(post("/api/v1/transporters/assignment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.assignments", hasSize(5)));
    }

    @Test
    void testTestCase2FromSpecification() throws Exception {
        List<LaneInputDto> lanes = Arrays.asList(
                new LaneInputDto(1L, "Chandigarh", "Shimla"),
                new LaneInputDto(2L, "Agra", "Kanpur"),
                new LaneInputDto(3L, "Varanasi", "Gorakhpur"),
                new LaneInputDto(4L, "Amritsar", "Ludhiana"),
                new LaneInputDto(5L, "Coimbatore", "Madurai")
        );

        List<TransporterInputDto> transporters = Arrays.asList(
                new TransporterInputDto(1L, "Transporter X", Arrays.asList(
                        new LaneQuoteInputDto(1L, 1000.0),
                        new LaneQuoteInputDto(2L, 1500.0)
                )),
                new TransporterInputDto(2L, "Transporter Y", Arrays.asList(
                        new LaneQuoteInputDto(3L, 2000.0),
                        new LaneQuoteInputDto(4L, 1800.0)
                )),
                new TransporterInputDto(3L, "Transporter Z", Arrays.asList(
                        new LaneQuoteInputDto(5L, 1200.0)
                )),
                new TransporterInputDto(4L, "Transporter W", Arrays.asList(
                        new LaneQuoteInputDto(1L, 1100.0),
                        new LaneQuoteInputDto(4L, 1750.0)
                ))
        );

        TransporterInputRequest inputRequest = new TransporterInputRequest(lanes, transporters);

        mockMvc.perform(post("/api/v1/transporters/input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")));

        AssignmentRequest assignmentRequest = new AssignmentRequest(3);

        mockMvc.perform(post("/api/v1/transporters/assignment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.totalCost", is(7500.0)))
                .andExpect(jsonPath("$.assignments", hasSize(5)))
                .andExpect(jsonPath("$.selectedTransporters", hasSize(3)))
                .andExpect(jsonPath("$.selectedTransporters", containsInAnyOrder(1, 2, 3)));
    }

    @Test
    void testInfeasibleAssignmentWithStrictLimit() throws Exception {
        List<LaneInputDto> lanes = Arrays.asList(
                new LaneInputDto(1L, "Chandigarh", "Shimla"),
                new LaneInputDto(2L, "Agra", "Kanpur"),
                new LaneInputDto(3L, "Varanasi", "Gorakhpur"),
                new LaneInputDto(4L, "Amritsar", "Ludhiana"),
                new LaneInputDto(5L, "Coimbatore", "Madurai")
        );

        List<TransporterInputDto> transporters = Arrays.asList(
                new TransporterInputDto(1L, "Transporter X", Arrays.asList(
                        new LaneQuoteInputDto(1L, 1000.0),
                        new LaneQuoteInputDto(2L, 1500.0)
                )),
                new TransporterInputDto(2L, "Transporter Y", Arrays.asList(
                        new LaneQuoteInputDto(3L, 2000.0),
                        new LaneQuoteInputDto(4L, 1800.0)
                )),
                new TransporterInputDto(3L, "Transporter Z", Arrays.asList(
                        new LaneQuoteInputDto(5L, 1200.0)
                ))
        );

        TransporterInputRequest inputRequest = new TransporterInputRequest(lanes, transporters);

        mockMvc.perform(post("/api/v1/transporters/input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputRequest)))
                .andExpect(status().isOk());

        AssignmentRequest assignmentRequest = new AssignmentRequest(2);

        mockMvc.perform(post("/api/v1/transporters/assignment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignmentRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status", is("error")));
    }

    @Test
    void testValidationFailureOnEmptyInput() throws Exception {
        TransporterInputRequest emptyRequest = new TransporterInputRequest(Collections.emptyList(), Collections.emptyList());

        mockMvc.perform(post("/api/v1/transporters/input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")));
    }

    @Test
    void testValidationFailureOnInvalidMaxTransporters() throws Exception {
        AssignmentRequest invalidRequest = new AssignmentRequest(0);

        mockMvc.perform(post("/api/v1/transporters/assignment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")));
    }
}

