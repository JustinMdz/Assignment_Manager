package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.FacultyDto;

import java.util.List;
import java.util.Optional;

public interface FacultyService {
    List<FacultyDto> getAllFaculties();

    Optional<FacultyDto> getById(Long id);

    FacultyDto create(FacultyDto facultyDto);

    Optional<FacultyDto> update(Long id, FacultyDto facultyDto);

    void delete(Long id);
}
