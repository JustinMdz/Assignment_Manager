package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Service.AssignmentService;

import java.util.Optional;

@RestController
@RequestMapping("api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

//
//    @GetMapping("getAllFaculties")
//    public ResponseEntity<List<FacultyDto>> getAllFaculties() {
//        List<FacultyDto> universities = facultyService.getAllFaculties();
//        return new ResponseEntity<>(universities, HttpStatus.OK);
//    }

    @GetMapping("/getById")
    public ResponseEntity<?> getAssignmentById(@RequestParam Long id) {
        try {
            AssignmentDto assignmentDto = assignmentService.findById(id);
            return new ResponseEntity<>(assignmentDto, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAssignment(@RequestBody AssignmentDto assignmentDto) {
        try {
            AssignmentDto createdAssignment = assignmentService.create(assignmentDto);
            return new ResponseEntity<>(createdAssignment, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable Long id, @RequestBody AssignmentDto assignmentDto) {
        try {
            Optional<AssignmentDto> updatedFaculty = assignmentService.update(id, assignmentDto);
            return updatedFaculty.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
