package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.DepartmentDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Service.DepartmentService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService, GenericMapperFactory mapperFactory) {
        this.departmentService = departmentService;
    }

    @GetMapping("getAllDepartments")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        List<DepartmentDto> universities = departmentService.getAllDepartments();
        return new ResponseEntity<>(universities, HttpStatus.OK);
    }

    @GetMapping("getMap")
    public ResponseEntity<Map<String, Object>> getDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "5") int limit) {
        Map<String, Object> response = departmentService.getDepartments(page, size, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("getPageable")
    public Page<DepartmentDto> getDepartments(Pageable pageable) {
        return departmentService.getPageDepartments(pageable);
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
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentDto departmentInput) {
        try {
            DepartmentDto createdUniversity = departmentService.create(departmentInput);
            return new ResponseEntity<>(createdUniversity, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto departmentInput) {
        try {
            Optional<DepartmentDto> updatedDepartment = departmentService.update(id, departmentInput);
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

