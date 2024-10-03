package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Service.CourseService;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("getAllCourses")
    public ResponseEntity<List<CourseDto>> getAllUniversities() {
        List<CourseDto> universities = courseService.getAllCourses();
        return new ResponseEntity<>(universities, HttpStatus.OK);
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getCourseById(@RequestParam Long id) {
        try {
            Optional<CourseDto> courseDto = courseService.getById(id);
            return new ResponseEntity<>(courseDto, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto) {
        try {
            CourseDto createdCourseDto = courseService.create(courseDto);
            return new ResponseEntity<>(createdCourseDto, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        try {
            Optional<CourseDto> updatedCourseDto = courseService.update(id, courseDto);
            return updatedCourseDto.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

