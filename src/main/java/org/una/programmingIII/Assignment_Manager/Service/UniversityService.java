package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.UniversityDto;

import java.util.List;
import java.util.Optional;

public interface UniversityService {
    UniversityDto create(UniversityDto universityDto);

    Optional<UniversityDto> getById(Long id);

    UniversityDto update(Long id, UniversityDto universityDto);

    void delete(Long id);
}


