package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.DepartmentDto;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    List<DepartmentDto> getAllDepartments();

    Optional<DepartmentDto> getById(Long id);

    DepartmentDto create(DepartmentDto departmentDto);

    Optional<DepartmentDto> update(Long id, DepartmentDto departmentDto);

    void delete(Long id);
}
