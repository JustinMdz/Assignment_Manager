package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.UniversityDto;

import java.util.List;
import java.util.Optional;

public interface UniversityService {
    List<UniversityDto> getAllUniversities();

    Optional<UniversityDto> getById(Long id);

    UniversityDto create(UniversityDto universityDto);

    Optional<UniversityDto> update(Long id, UniversityDto universityDto);

    void delete(Long id);
}


