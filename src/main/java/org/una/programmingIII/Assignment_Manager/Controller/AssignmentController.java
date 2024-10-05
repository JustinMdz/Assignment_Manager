package org.una.programmingIII.Assignment_Manager.Controller;

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

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/assignments")
public class AssignmentController {
    private final GenericMapper< AssignmentDto,AssignmentInput> assignmentMapper;
    private final AssignmentService assignmentService;
    @Autowired
    AssignmentController(AssignmentService assignmentService,  GenericMapperFactory mapperFactory) {
        this.assignmentService = assignmentService;
        this.assignmentMapper = mapperFactory.createMapper(AssignmentDto.class, AssignmentInput.class);
    }

    @GetMapping("getMap")
    public ResponseEntity<Map<String, Object>> getAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "5") int limit) {
        Map<String, Object> response = assignmentService.getAssignments(page, size, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("getPageable")
    public Page<AssignmentDto> getAssignments(Pageable pageable) {
        return assignmentService.getPageAssignment(pageable);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
