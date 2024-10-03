package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.FacultyDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Service.FacultyService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/faculties")
public class FacultyController {
    @Autowired
    private FacultyService facultyService;

    @GetMapping("getAllFaculties")
    public ResponseEntity<List<FacultyDto>> getAllFaculties() {
        List<FacultyDto> universities = facultyService.getAllFaculties();
        return new ResponseEntity<>(universities, HttpStatus.OK);
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getFacultyById(@RequestParam Long id) {
        try {
            Optional<FacultyDto> facultyDto = facultyService.getById(id);
            return new ResponseEntity<>(facultyDto, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUniversity(@RequestBody FacultyDto facultyDto) {
        try {
            FacultyDto createdFaculty = facultyService.create(facultyDto);
            return new ResponseEntity<>(createdFaculty, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUniversity(@PathVariable Long id, @RequestBody FacultyDto facultyDto) {
        try {
            Optional<FacultyDto> updatedFaculty = facultyService.update(id, facultyDto);
            return updatedFaculty.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

