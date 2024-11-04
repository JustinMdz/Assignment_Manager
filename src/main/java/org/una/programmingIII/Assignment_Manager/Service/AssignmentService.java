package org.una.programmingIII.Assignment_Manager.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.una.programmingIII.Assignment_Manager.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager.Model.File;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AssignmentService {

    Map<String, Object> getAssignments(int page, int size, int limit);

    Page<AssignmentDto> getPageAssignment(Pageable pageable);

    AssignmentDto findById(Long id);

    AssignmentDto create(AssignmentDto assignment);

    Optional<AssignmentDto> update(Long id, AssignmentDto assignment);

    void delete(Long id);

    void insertFileToAssignment(Long id, File file);
    List<AssignmentDto> findAllByCourseId(Long courseId);
    List<AssignmentDto> findByCourseIdAndAddress(Long courseId, String address);

}
