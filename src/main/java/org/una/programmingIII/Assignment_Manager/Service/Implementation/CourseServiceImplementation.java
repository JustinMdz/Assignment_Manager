package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager.Model.Course;
import org.una.programmingIII.Assignment_Manager.Repository.CourseRepository;
import org.una.programmingIII.Assignment_Manager.Service.CourseService;

import jakarta.persistence.EntityNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getCourses(int page, int size, int limit) {
        Page<Course> coursePage = courseRepository.findAll(PageRequest.of(page, size));
        coursePage.forEach(course -> {
            course.setStudents(limitListOrDefault(course.getStudents(), limit));
        });

        Map<String, Object> response = new HashMap<>();
        response.put("courses", coursePage.map(this::convertToDto).getContent());
        response.put("totalPages", coursePage.getTotalPages());
        response.put("totalElements", coursePage.getTotalElements());
        return response;
    }

    @Override
    public Page<CourseDto> getPageCourses(Pageable pageable) {
        Page<Course> coursePage = courseRepository.findAll(pageable);
        return coursePage.map(courseMapper::convertToDTO);
    }

    @Override
    public Optional<CourseDto> getById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::convertToDTO);
    }

    @Override
    public Optional<CourseDto> update(Long id, CourseDto courseDto) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                    Course updatedCourse = courseMapper.convertToEntity(courseDto);
                    updatedCourse.setId(id);
                    Course savedCourse = courseRepository.save(updatedCourse);
                    return Optional.of(courseMapper.convertToDTO(savedCourse));
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

    private <T> List<T> limitListOrDefault(List<T> list, int limit) {
        return list == null ? new ArrayList<>() : limitList(list, limit);
    }

    private <T> List<T> limitList(List<T> list, int limit) {
        return list.stream().limit(limit).collect(Collectors.toList());
    }

    private CourseDto convertToDto(Course course) {
        return courseMapper.convertToDTO(course);
    }
}
