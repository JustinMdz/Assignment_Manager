package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager.Model.Submission;

import java.util.Map;
import java.util.Optional;

public interface SubmissionService {
    void deleteById(Long id);

    Optional<SubmissionDto> findById(Long id);

    SubmissionDto create(SubmissionDto submissionDto);

    Optional<SubmissionDto> update(Long id, SubmissionDto submissionDto);

    Map<String, Object> getAllSubmissions(int page, int size);
}
