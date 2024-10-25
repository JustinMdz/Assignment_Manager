package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.CourseContentDto;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.CourseContent;
import org.una.programmingIII.Assignment_Manager.Model.File;
import org.una.programmingIII.Assignment_Manager.Repository.CourseContentRepository;
import org.una.programmingIII.Assignment_Manager.Service.CourseContentService;
import org.una.programmingIII.Assignment_Manager.Service.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseContentServiceImplementation implements CourseContentService {
    private final CourseContentRepository courseContentRepository;
    private final GenericMapper<CourseContent, CourseContentDto> courseContentMapper;
    @Autowired
    public CourseContentServiceImplementation(CourseContentRepository courseContentRepository, GenericMapperFactory mapperFactory ) {
        this.courseContentRepository = courseContentRepository;
        this.courseContentMapper = mapperFactory.createMapper(CourseContent.class, CourseContentDto.class);
    }
    @Override
    public CourseContentDto findByCourseContentId(Long courseContentId) {
        return courseContentMapper.convertToDTO(courseContentRepository.findById(courseContentId).orElseThrow(()->new ElementNotFoundException("Course not found with id: " + courseContentId)));
    }

    @Override
    public CourseContentDto create(CourseContentDto courseContentDto) {
        courseContentDto.setCreatedAt(java.time.LocalDate.now());
        courseContentDto.setUpdatedAt(java.time.LocalDate.now());
        return courseContentMapper.convertToDTO(courseContentRepository.save(courseContentMapper.convertToEntity(courseContentDto)));
    }

@Override
public CourseContentDto update(CourseContentDto courseContentDto, Long id) {
    if (!courseContentRepository.existsById(id)) {
        throw new ElementNotFoundException("Course not found with id: " + id);
    }
    courseContentDto.setUpdatedAt(java.time.LocalDate.now());
    CourseContent newCourseContent = courseContentMapper.convertToEntity(courseContentDto);
    CourseContent courseContent = courseContentRepository.findById(id)
        .orElseThrow(() -> new ElementNotFoundException("Course not found with id: " + id));

    List<File> combinedFiles = new ArrayList<>(newCourseContent.getFiles());
    courseContent.getFiles().stream()
        .filter(file -> !combinedFiles.contains(file))
        .forEach(combinedFiles::add);
    newCourseContent.setFiles(combinedFiles);

    return courseContentMapper.convertToDTO(courseContentRepository.save(newCourseContent));
}

public List<CourseContentDto> findAllByCourseId(Long courseId) {
    List<CourseContent> courseContents = (List<CourseContent>) courseContentRepository.findAllByCourseId(courseId);
    List<CourseContentDto> courseContentDtoList = new ArrayList<>();
    for (CourseContent courseContent : courseContents) {
        courseContentDtoList.add(courseContentMapper.convertToDTO(courseContent));
    }
    return courseContentDtoList;
}

    @Override
    public void delete(Long id) {
        if (!courseContentRepository.existsById(id)) {
            throw new ElementNotFoundException("Course not found with id: " + id);
        }
        courseContentRepository.deleteById(id);}

    @Override
    public void insertFileToCourseContent(Long id, File file) {
        CourseContent courseContent = courseContentRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Course not found with id: " + id));
        courseContent.getFiles().add(file);
        courseContentRepository.save(courseContent);
    }
}
