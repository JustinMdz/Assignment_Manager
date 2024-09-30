package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager.Model.Course;
import org.una.programmingIII.Assignment_Manager.Repository.CourseRepository;
import org.una.programmingIII.Assignment_Manager.Service.CourseService;

import jakarta.persistence.EntityNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;

import java.util.Optional;

@Service
public class CourseServiceImplementation implements CourseService {

    @Autowired
    private CourseRepository courseRepository;
    private final GenericMapper<Course, CourseDto> courseMapper;

    public CourseServiceImplementation(GenericMapperFactory mapperFactory) {
        this.courseMapper = mapperFactory.createMapper(Course.class, CourseDto.class);
    }

    @Override
    public CourseDto create(CourseDto courseDto) {
        Course course = courseMapper.convertToEntity(courseDto);
        course = courseRepository.save(course);
        return courseMapper.convertToDTO(course);
    }

    @Override
    public Optional<CourseDto> getById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::convertToDTO);
    }

    @Override
    public CourseDto update(Long id, CourseDto courseDto) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                    Course updatedCourse = courseMapper.convertToEntity(courseDto);
                    updatedCourse.setId(id);
                    Course savedCourse = courseRepository.save(updatedCourse);
                    return courseMapper.convertToDTO(savedCourse);
                })
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id " + id));
    }

    @Override
    public void delete(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Course not found with id " + id);
        }
    }
}
