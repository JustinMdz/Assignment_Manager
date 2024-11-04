package org.una.programmingIII.Assignment_Manager.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.Input.SubmissionInput;
import org.una.programmingIII.Assignment_Manager.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.Submission;
import org.una.programmingIII.Assignment_Manager.Service.SubmissionService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("api/submissions")
public class SubmissionController {
    private final GenericMapper<SubmissionInput, SubmissionDto> submissionMapper;
    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService, GenericMapperFactory mapperFactory) {
        this.submissionService = submissionService;
        this.submissionMapper = mapperFactory.createMapper(SubmissionInput.class, SubmissionDto.class);
    }

    @Operation(summary = "Get submissions as a map", description = "Retrieve submissions with pagination and limit as a map.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("getMap")
    public ResponseEntity<Map<String, Object>> getSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "5") int limit) {
        Map<String, Object> response = submissionService.getSubmissions(page, size, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get submissions as pageable", description = "Retrieve submissions with pagination as pageable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("getPageable")
    public Page<SubmissionDto> getSubmissions(Pageable pageable) {
        return submissionService.getPageSubmissions(pageable);
    }

    @Operation(summary = "Get submission by ID", description = "Retrieve a submission by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SubmissionDto.class))),
            @ApiResponse(responseCode = "404", description = "Submission not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("/getById")
    public ResponseEntity<?> getSubmissionById(@RequestParam Long id) {
        try {
            Optional<SubmissionDto> submissionDto = submissionService.findById(id);
            return new ResponseEntity<>(submissionDto, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create a new submission", description = "Create a new submission with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Submission created successfully",
                    content = @Content(schema = @Schema(implementation = SubmissionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PostMapping("/")
    public ResponseEntity<?> createSubmission(@RequestBody SubmissionInput submissionInput) {
        try {
            SubmissionDto createdSubmissionDto = submissionService.create(submissionMapper.convertToDTO(submissionInput));
            return new ResponseEntity<>(createdSubmissionDto, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update a submission", description = "Update an existing submission with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission updated successfully",
                    content = @Content(schema = @Schema(implementation = SubmissionDto.class))),
            @ApiResponse(responseCode = "404", description = "Submission not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubmission(@PathVariable Long id, @RequestBody SubmissionInput submissionInput) {
        try {
            Optional<SubmissionDto> updatedSubmissionDto = submissionService.update(id, submissionMapper.convertToDTO(submissionInput));
            return updatedSubmissionDto.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a submission", description = "Delete an existing submission by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Submission deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Submission not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get a submission by assignment", description = "Retrieve a submission by its assignment ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SubmissionDto.class))),
            @ApiResponse(responseCode = "404", description = "Submission not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("/getByAssignmentId/{assignmentId}")
    public ResponseEntity<?> getSubmissionsByAssignmentId(@PathVariable Long assignmentId) {
        try {
            System.out.println("Assignment ID: " + assignmentId);
            List<SubmissionDto> submissionsDto = submissionService.getSubmissionsByAssignmentId(assignmentId);
            return new ResponseEntity<>(submissionsDto, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get a submission by assignment and student", description = "Retrieve a submission by its assignment and student ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SubmissionDto.class))),
            @ApiResponse(responseCode = "404", description = "Submission not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("/getByAssignmentIdAndStudentId/{assignmentId}/{userId}")
    public ResponseEntity<?> getSubmissionByAssignmentIdAndStudentId(@PathVariable Long assignmentId, @PathVariable Long userId) {
        try {
            Optional<SubmissionDto> submissionDto = submissionService.getSubmissionByAssignmentIdAndStudentId(assignmentId, userId);
            return new ResponseEntity<>(submissionDto, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}