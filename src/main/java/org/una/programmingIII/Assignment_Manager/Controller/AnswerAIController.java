package org.una.programmingIII.Assignment_Manager.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Service.AnswerAIService;

@RestController
@RequestMapping("/api/answerAI")
public class AnswerAIController {
    private final AnswerAIService answerAIService;
    @Autowired
    public AnswerAIController(AnswerAIService answerAIService) {
        this.answerAIService = answerAIService;
    }

@Operation(summary = "Find all answers", description = "Retrieves all answers from the AI service.")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Answers retrieved successfully"),
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
})
@GetMapping("/")
public ResponseEntity<?> findAll() {
    try {
        return new ResponseEntity<>(answerAIService.findAll(), HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
@Operation(summary = "Find an answer by id", description = "Retrieves an answer from the AI service by its id.")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Answer retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Answer not found",
            content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
})
@GetMapping("/findById/{id}")
public ResponseEntity<?> findById(@PathVariable(name = "id")Long id) {
    try {
        return new ResponseEntity<>(answerAIService.getById(id), HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}}
