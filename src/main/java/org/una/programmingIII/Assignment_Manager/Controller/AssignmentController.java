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
import org.una.programmingIII.Assignment_Manager.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Dto.Input.AssignmentInput;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Service.AssignmentService;
import org.una.programmingIII.Assignment_Manager.Service.FileService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    private final GenericMapper< AssignmentDto,AssignmentInput> assignmentMapper;
    private final AssignmentService assignmentService;
    private final FileService fileService;
    @Autowired
    AssignmentController(AssignmentService assignmentService,  GenericMapperFactory mapperFactory, FileService fileService) {
        this.assignmentService = assignmentService;
        this.assignmentMapper = mapperFactory.createMapper(AssignmentDto.class, AssignmentInput.class);
        this.fileService = fileService;
    }

    @Operation(summary = "Get assignments as a map", description = "Retrieves assignments in a paginated format as a map.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @GetMapping("getMap")
    public ResponseEntity<Map<String, Object>> getAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "5") int limit) {
        Map<String, Object> response = assignmentService.getAssignments(page, size, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "Get assignments as pageable", description = "Retrieves assignments in a paginated format.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @GetMapping("getPageable")
    public Page<AssignmentDto> getAssignments(Pageable pageable) {
        return assignmentService.getPageAssignment(pageable);
    }
    @Operation(summary = "Get assignment by ID", description = "Retrieves an assignment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Assignment not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable(name = "id") Long id) {
        try {
            AssignmentDto assignmentDto = assignmentService.findById(id);
            return new ResponseEntity<>(assignmentDto, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get assignments by course ID", description = "Retrieves assignments by the course ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Assignments not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @GetMapping("/getByCourseId/{id}")
    public ResponseEntity<?> getAssignmentsByCourseId(@PathVariable(name = "id") Long courseId) {
        try{
            return new ResponseEntity<>(assignmentService.findAllByCourseId(courseId), HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get assignment by course ID and address", description = "Retrieves an assignment by the course ID and address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Assignment not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @GetMapping("/getByCourseIdAndAddress/{courseId}/{address}")
    public ResponseEntity<?> getAssignmentByCourseIdAndAddress(@PathVariable(name = "courseId") Long courseId, @PathVariable(name = "address") String address) {
        try {
            AssignmentDto assignmentDto = assignmentService.findByCourseIdAndAddress(courseId, address);
            return new ResponseEntity<>(assignmentDto, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Create assignment", description = "Creates a new assignment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Assignment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @PostMapping("/create")
    public ResponseEntity<?> createAssignment(@RequestBody  AssignmentInput assignmentInput) {
        try {

            AssignmentDto createdAssignment = assignmentService.create(assignmentMapper.convertToEntity(assignmentInput));
            return new ResponseEntity<>(createdAssignment, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update assignment", description = "Updates an existing assignment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Assignment not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable Long id, @RequestBody AssignmentInput assignmentInput) {
        try {
            Optional<AssignmentDto> updatedFaculty = assignmentService.update(id, assignmentMapper.convertToEntity(assignmentInput));
            return updatedFaculty.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete assignment", description = "Deletes an assignment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assignment deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        fileService.deleteFilesAndAssignment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
