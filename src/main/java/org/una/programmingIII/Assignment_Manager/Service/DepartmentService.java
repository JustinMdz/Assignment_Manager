package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.DepartmentDto;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    DepartmentDto create(DepartmentDto departmentDto);

    Optional<DepartmentDto> getById(Long id);

    DepartmentDto update(Long id, DepartmentDto departmentDto);

    void delete(Long id);
}
