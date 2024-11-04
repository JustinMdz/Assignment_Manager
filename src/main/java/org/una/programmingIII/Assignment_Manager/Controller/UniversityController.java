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
    import org.una.programmingIII.Assignment_Manager.Dto.UniversityDto;
    import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
    import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
    import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
    import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
    import org.una.programmingIII.Assignment_Manager.Service.UniversityService;

    import java.util.List;
    import java.util.Map;
    import java.util.Optional;

 @RestController
@RequestMapping("/api/universities")
public class UniversityController {
    private final UniversityService universityService;

    @Autowired
    public UniversityController(UniversityService universityService, GenericMapperFactory universityMapper) {
        this.universityService = universityService;
    }

    @Operation(summary = "Get all universities", description = "Retrieve a list of all universities.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Universities retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UniversityDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("getUniversities")
    public ResponseEntity<List<UniversityDto>> getUniversities() {
        List<UniversityDto> universities = universityService.getAllUniversities();
        return new ResponseEntity<>(universities, HttpStatus.OK);
    }

    @Operation(summary = "Get universities with pagination", description = "Retrieve a paginated list of universities.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Universities retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UniversityDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("getMap")
    public ResponseEntity<Map<String, Object>> getAllUniversities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "5") int limit) {
        Map<String, Object> response = universityService.getUniversities(page, size, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get universities with pageable", description = "Retrieve a pageable list of universities.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Universities retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UniversityDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("getPageable")
    public Page<UniversityDto> getAllUniversities(Pageable pageable) {
        return universityService.getPageUniversities(pageable);
    }

    @Operation(summary = "Get university by ID", description = "Retrieve a university by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "University retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UniversityDto.class))),
            @ApiResponse(responseCode = "404", description = "University not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("/getById")
    public ResponseEntity<?> getUniversityById(@RequestParam Long id) {
        try {
            Optional<UniversityDto> universityDto = universityService.getById(id);
            return new ResponseEntity<>(universityDto, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create a new university", description = "Create a new university.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "University created successfully",
                    content = @Content(schema = @Schema(implementation = UniversityDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<?> createUniversity(@RequestBody UniversityDto universityInput) {
        try {
            UniversityDto createdUniversity = universityService.create(universityInput);
            return new ResponseEntity<>(createdUniversity, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update a university", description = "Update an existing university.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "University updated successfully",
                    content = @Content(schema = @Schema(implementation = UniversityDto.class))),
            @ApiResponse(responseCode = "404", description = "University not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUniversity(@PathVariable Long id, @RequestBody UniversityDto universityInput) {
        try {
            Optional<UniversityDto> updatedUniversity = universityService.update(id, universityInput);
            return updatedUniversity.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a university", description = "Delete an existing university.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "University deleted successfully"),
            @ApiResponse(responseCode = "404", description = "University not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        universityService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}