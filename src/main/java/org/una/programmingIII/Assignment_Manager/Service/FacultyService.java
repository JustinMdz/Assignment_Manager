package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.FacultyDto;

import java.util.List;
import java.util.Optional;

public interface FacultyService {
    FacultyDto create(FacultyDto facultyDto);

    Optional<FacultyDto> getById(Long id);

    FacultyDto update(Long id, FacultyDto facultyDto);

    void delete(Long id);
}
