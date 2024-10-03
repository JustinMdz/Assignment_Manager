package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.AssignmentDto;

import java.util.Optional;

public interface AssignmentService {
    AssignmentDto findById(Long id);

    AssignmentDto create(AssignmentDto assignment);

    Optional<AssignmentDto> update(Long id, AssignmentDto assignment);

    void delete(Long id);
}
