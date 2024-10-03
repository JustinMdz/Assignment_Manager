package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Service.UniversityService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/universities")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @GetMapping("getAllUniversities")
    public ResponseEntity<List<UniversityDto>> getAllUniversities() {
        List<UniversityDto> universities = universityService.getAllUniversities();
        return new ResponseEntity<>(universities, HttpStatus.OK);
    }

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

    @PostMapping("/create")
    public ResponseEntity<?> createUniversity(@RequestBody UniversityDto universityDto) {
        try {
            UniversityDto createdUniversity = universityService.create(universityDto);
            return new ResponseEntity<>(createdUniversity, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUniversity(@PathVariable Long id, @RequestBody UniversityDto universityDto) {
        try {
            Optional<UniversityDto> updatedUniversity = universityService.update(id, universityDto);
            return updatedUniversity.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        universityService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
