package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.Input.SubmissionInput;
import org.una.programmingIII.Assignment_Manager.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.Submission;
import org.una.programmingIII.Assignment_Manager.Service.SubmissionService;

import java.util.Map;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("api/submissions")
public class SubmissionController {
    private final GenericMapper<SubmissionInput, SubmissionDto> submissionMapper;
    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService, GenericMapperFactory mapperFactory) {
        this.submissionService = submissionService;
        this.submissionMapper = mapperFactory.createMapper(SubmissionInput.class, SubmissionDto.class);
    }

                                @GetMapping("getMap")
    public ResponseEntity<Map<String, Object>> getSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "5") int limit) {
        Map<String, Object> response = submissionService.getSubmissions(page, size, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("getPageable")
    public Page<SubmissionDto> getSubmissions(Pageable pageable) {
        return submissionService.getPageSubmissions(pageable);
    }


    @GetMapping("/getById")
    public ResponseEntity<?> getSubmissionById(@RequestParam Long id) {
        try {
            Optional<SubmissionDto> submissionDto = submissionService.findById(id);
            return new ResponseEntity<>(submissionDto, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createSubmission(@RequestBody SubmissionInput submissionInput) {
        try {
            SubmissionDto createdSubmissionDto = submissionService.create(submissionMapper.convertToDTO(submissionInput));
            return new ResponseEntity<>(createdSubmissionDto, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubmission(@PathVariable Long id, @RequestBody SubmissionInput submissionInput) {
        try {
            Optional<SubmissionDto> updatedSubmissionDto = submissionService.update(id, submissionMapper.convertToDTO(submissionInput));
            return updatedSubmissionDto.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
