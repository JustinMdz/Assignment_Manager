package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager.Model.Submission;

import java.util.Map;

public interface SubmissionService {
    void deleteById(Long id);
    SubmissionDto findById(Long id);
    SubmissionDto create(Submission submission);
    SubmissionDto update(Submission submission);
    Map<String, Object> getAllSubmissions(int page, int size);
}
