package org.una.programmingIII.Assignment_Manager.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.CourseContentDto;
import org.una.programmingIII.Assignment_Manager.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager.Dto.Input.CourseContentInput;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Service.CourseContentService;

@RestController
@RequestMapping("/api/courseContents")
public class CourseContentController {
    private final CourseContentService courseContentService;
    private final GenericMapper<CourseContentDto, CourseContentInput> courseContentMapper;

    @Autowired
    public CourseContentController(CourseContentService courseContentService, GenericMapperFactory mapperFactory) {
        this.courseContentService = courseContentService;
        this.courseContentMapper = mapperFactory.createMapper(CourseContentDto.class, CourseContentInput.class);
    }

    @Operation(summary = "Get all CourseContents by ID Course", description = "This endpoint returns a list of all registered CourseContents by Course id in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CourseContents retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CourseContentDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("getAllByCourseId/{id}")
    public ResponseEntity<?> getAllCourseContents(@PathVariable(name = "id") Long id) {
        try {
            return new ResponseEntity<>(courseContentService.findAllByCourseId(id), HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Find CourseContent by email", description = "This endpoint retrieves a CourseContent by their id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CourseContent found successfully",
                    content = @Content(schema = @Schema(implementation = CourseContentDto.class))),
            @ApiResponse(responseCode = "404", description = "CourseContent not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Long id) {
        try {
            return new ResponseEntity<>(courseContentService.findByCourseContentId(id), HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
@Operation(summary = "Create a new CourseContent", description = "This endpoint allows the creation of a new CourseContent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CourseContent created successfully",
                    content = @Content(schema = @Schema(implementation = CourseContentDto.class))),
            @ApiResponse(responseCode = "201", description = "CourseContent created",
                    content = @Content(schema = @Schema(implementation = CourseContentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody CourseContentInput courseContentInput) {
    System.out.println("Received CourseContentInput: " + courseContentInput);
        try {
            CourseContentDto courseDto = courseContentMapper.convertToEntity(courseContentInput);
            return new ResponseEntity<>(courseContentService.create(courseDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Update CourseContent", description = "This endpoint allows to update a CourseContent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CourseContent updated successfully",
                    content = @Content(schema = @Schema(implementation = CourseContentDto.class))),
            @ApiResponse(responseCode = "404", description = "CourseContent not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @RequestBody CourseContentInput courseContentInput) {
        try {
            return new ResponseEntity<>(courseContentService.update(courseContentMapper.convertToEntity(courseContentInput), id), HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Delete CourseContent", description = "This endpoint allows to delete a CourseContent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "CourseContent deleted successfully",
                    content = @Content(schema = @Schema(implementation = CourseContentDto.class))),
            @ApiResponse(responseCode = "404", description = "CourseContent not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        try {
            courseContentService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
