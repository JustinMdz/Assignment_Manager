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
import org.una.programmingIII.Assignment_Manager.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Service.CareerService;

@RestController
@RequestMapping("/api/careers")
public class CareerController {

    private final CareerService careerService;

    @Autowired
    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @Operation(summary = "Create a new Career", description = "This endpoint allows the creation of a new Career.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Career created successfully",
                    content = @Content(schema = @Schema(implementation = CareerDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PostMapping("/")
    public ResponseEntity<CareerDto> createCareer(@RequestBody CareerDto careerDto) {
        CareerDto createdCareer = careerService.create(careerDto);
        return new ResponseEntity<>(createdCareer, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete Career", description = "This endpoint allows to delete a Career.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Career deleted successfully",
                    content = @Content(schema = @Schema(implementation = CareerDto.class))),
            @ApiResponse(responseCode = "404", description = "Career not found",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCareer(@PathVariable Long id) {
        careerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
