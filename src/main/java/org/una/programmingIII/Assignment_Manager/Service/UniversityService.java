package org.una.programmingIII.Assignment_Manager.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.una.programmingIII.Assignment_Manager.Dto.UniversityDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UniversityService {
    List<UniversityDto> getAllUniversities();

    Optional<UniversityDto> getById(Long id);

    UniversityDto create(UniversityDto universityDto);

    Optional<UniversityDto> update(Long id, UniversityDto universityDto);

    void delete(Long id);

    Map<String, Object> getUniversities(int page, int size, int limit);

    Page<UniversityDto> getPageUniversities(Pageable pageable);

}


