
package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager.Dto.FacultyDto;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<CourseDto> getAllCourses();

    Optional<CourseDto> getById(Long id);

    CourseDto create(CourseDto courseDto);

    Optional<CourseDto> update(Long id, CourseDto courseDto);

    void delete(Long id);
}
