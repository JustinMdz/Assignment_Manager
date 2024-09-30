package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.AssignmentDto;

public interface AssignmentService {
    AssignmentDto findById(Long id);
    AssignmentDto create(AssignmentDto assignment);
    AssignmentDto update(AssignmentDto assignment);
    void delete(Long id);
}
