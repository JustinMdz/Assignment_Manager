
package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.CourseDto;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    CourseDto create(CourseDto courseDto);

    Optional<CourseDto> getById(Long id);

    CourseDto update(Long id, CourseDto courseDto);

    void delete(Long id);
}
