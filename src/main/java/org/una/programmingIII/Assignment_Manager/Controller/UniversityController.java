package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.Input.UniversityInput;
import org.una.programmingIII.Assignment_Manager.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.University;
import org.una.programmingIII.Assignment_Manager.Service.UniversityService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/universities")
public class UniversityController {


    private final UniversityService universityService;
    private final GenericMapper<UniversityInput, UniversityDto> universityMapper;

    @Autowired
    public UniversityController(UniversityService universityService, GenericMapperFactory universityMapper) {
        this.universityService = universityService;
        this.universityMapper = universityMapper.createMapper(UniversityInput.class, UniversityDto.class);
    }
    @GetMapping("getUniversities")
    public ResponseEntity<List<UniversityDto>> getUniversities() {
        List<UniversityDto> universities = universityService.getAllUniversities();
        return new ResponseEntity<>(universities, HttpStatus.OK);
    }

    @GetMapping("getMap")
    public ResponseEntity<Map<String, Object>> getAllUniversities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "5") int limit) {
        Map<String, Object> response = universityService.getUniversities(page, size, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("getPageable")
    public Page<UniversityDto> getAllUniversities(Pageable pageable) {
        return universityService.getPageUniversities(pageable);
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
    public ResponseEntity<?> createUniversity(@RequestBody UniversityInput universityInput) {
        try {
            UniversityDto createdUniversity = universityService.create(universityMapper.convertToDTO(universityInput));
            return new ResponseEntity<>(createdUniversity, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUniversity(@PathVariable Long id, @RequestBody UniversityInput universityInput) {
        try {
            Optional<UniversityDto> updatedUniversity = universityService.update(id, universityMapper.convertToDTO(universityInput));
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
