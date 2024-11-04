package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.CourseContentDto;
import org.una.programmingIII.Assignment_Manager.Model.File;

import java.util.List;

public interface CourseContentService {
    CourseContentDto findByCourseContentId(Long courseContentId);
    CourseContentDto create(CourseContentDto courseContentDto);
    CourseContentDto update(CourseContentDto courseContentDto, Long id);
    List<CourseContentDto> findAllByCourseId(Long courseId);
    void insertFileToCourseContent(Long id, File file);
    void delete(Long id);
}
