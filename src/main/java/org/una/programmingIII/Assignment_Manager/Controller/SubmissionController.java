package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Service.SubmissionService;

import java.util.Optional;


@RestController
@RequestMapping("api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;


//    @GetMapping("getAllUniversities")
//    public ResponseEntity<List<UniversityDto>> getAllUniversities() {
//        List<UniversityDto> universities = submissionService.getAllSubmissions();
//        return new ResponseEntity<>(universities, HttpStatus.OK);
//    }

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

    @PostMapping("/create")
    public ResponseEntity<?> createSubmission(@RequestBody SubmissionDto submissionDto) {
        try {
            SubmissionDto createdSubmissionDto = submissionService.create(submissionDto);
            return new ResponseEntity<>(createdSubmissionDto, HttpStatus.CREATED);
        } catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubmission(@PathVariable Long id, @RequestBody SubmissionDto submissionDto) {
        try {
            Optional<SubmissionDto> updatedSubmissionDto = submissionService.update(id, submissionDto);
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
