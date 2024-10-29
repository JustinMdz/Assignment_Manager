package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager.Service.CareerService;

@RestController
@RequestMapping("/api/careers/")
public class CareerController {

    private final CareerService careerService;

    @Autowired
    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @PostMapping
    public ResponseEntity<CareerDto> createCareer(@RequestBody CareerDto careerDto) {
        CareerDto createdCareer = careerService.create(careerDto);
        return new ResponseEntity<>(createdCareer, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCareer(@PathVariable Long id) {
        careerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
