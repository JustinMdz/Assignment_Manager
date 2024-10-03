package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.DepartmentDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Service.DepartmentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("getAllDepartments")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        List<DepartmentDto> universities = departmentService.getAllDepartments();
        return new ResponseEntity<>(universities, HttpStatus.OK);
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getDepartmentById(@RequestParam Long id) {
        try {
            Optional<DepartmentDto> departmentDto = departmentService.getById(id);
            return new ResponseEntity<>(departmentDto, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentDto departmentDto) {
        try {
            DepartmentDto createdUniversity = departmentService.create(departmentDto);
            return new ResponseEntity<>(createdUniversity, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto departmentDto) {
        try {
            Optional<DepartmentDto> updatedDepartment = departmentService.update(id, departmentDto);
            return updatedDepartment.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

