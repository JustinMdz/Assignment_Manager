package org.una.programmingIII.Assignment_Manager.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.una.programmingIII.Assignment_Manager.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SubmissionService {
    void deleteById(Long id);

    Optional<SubmissionDto> findById(Long id);

    SubmissionDto create(SubmissionDto submissionDto);

    Optional<SubmissionDto> update(Long id, SubmissionDto submissionDto);

    Map<String, Object> getSubmissions(int page, int size, int limit);

    List<SubmissionDto> getSubmissionsByAssignmentId(Long assignmentId);

    Page<SubmissionDto> getPageSubmissions(Pageable pageable);
}
