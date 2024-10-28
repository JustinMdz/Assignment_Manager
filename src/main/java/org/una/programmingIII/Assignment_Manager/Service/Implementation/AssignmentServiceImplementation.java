package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.Assignment;
import org.una.programmingIII.Assignment_Manager.Model.File;
import org.una.programmingIII.Assignment_Manager.Repository.AssignmentRepository;
import org.una.programmingIII.Assignment_Manager.Service.AssignmentService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImplementation implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final GenericMapper<Assignment, AssignmentDto> assignmentMapper;

    @Autowired
    public AssignmentServiceImplementation(AssignmentRepository assignmentRepository, GenericMapperFactory mapperFactory) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = mapperFactory.createMapper(Assignment.class, AssignmentDto.class);
    }

    @Override
    public Map<String, Object> getAssignments(int page, int size, int limit) {
        Page<Assignment> assignmentPage = assignmentRepository.findAll(PageRequest.of(page, size));

        Map<String, Object> response = new HashMap<>();
        response.put("assignments", assignmentPage.map(this::convertToDto).getContent());
        response.put("totalPages", assignmentPage.getTotalPages());
        response.put("totalElements", assignmentPage.getTotalElements());
        return response;
    }

    @Override
    public Page<AssignmentDto> getPageAssignment(Pageable pageable) {
        Page<Assignment> assignmentPage = assignmentRepository.findAll(pageable);
        return assignmentPage.map(assignmentMapper::convertToDTO);
    }

    @Override
    public AssignmentDto findById(Long id) {
        return assignmentMapper.convertToDTO(assignmentRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("FeedingSchedule not found with id: " + id)));
    }

    @Override
    public void delete(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new EntityNotFoundException("University not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
    }

    @Override
    public void insertFileToAssignment(Long id, File file) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("University not found with id: " + id));
        assignment.getFiles().add(file);
        assignmentRepository.save(assignment);
    }

    @Override
    public AssignmentDto create(AssignmentDto assignmentDto) {
        return assignmentMapper.convertToDTO(assignmentRepository.save(assignmentMapper.convertToEntity(assignmentDto)));
    }

    @Override
    public Optional<AssignmentDto> update(Long id, AssignmentDto assignmentDto) {
        if (!assignmentRepository.existsById(id)) {
            throw new ElementNotFoundException("University not found with id: " + id);
        }
        assignmentDto.setUpdatedAt(LocalDate.now());
        Assignment newAssignment = assignmentMapper.convertToEntity(assignmentDto);
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("University not found with id: " + id));
        List<File> combinedFiles = new ArrayList<>(newAssignment.getFiles());
        assignment.getFiles().stream()
                .filter(file -> !combinedFiles.contains(file))
                .forEach(combinedFiles::add);
        newAssignment.setFiles(combinedFiles);
        return Optional.ofNullable(assignmentMapper.convertToDTO(assignmentRepository.save(newAssignment)));
    }
    @Override
    public List<AssignmentDto> findAllByCourseId(Long courseId) {
        List<Assignment> assignments = (List<Assignment>) assignmentRepository.findAllByCourseId(courseId);
        return assignments.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    @Override
    public AssignmentDto findByCourseIdAndAddress(Long courseId, String address) {
        if (courseId == null || address == null) {
            throw new IllegalArgumentException("CourseId and address must not be null");
        }

Assignment assignment = assignmentRepository.findByCourseIdAndAddress(courseId, address);
if (assignment == null) {
    throw new ElementNotFoundException("Assignment not found with courseId: " + courseId + " and address: " + address);
}
return convertToDto(assignment);
    }
    private <T> List<T> limitListOrDefault(List<T> list, int limit) {
        return list == null ? new ArrayList<>() : limitList(list, limit);
    }

    private <T> List<T> limitList(List<T> list, int limit) {
        return list.stream().limit(limit).collect(Collectors.toList());
    }

    private AssignmentDto convertToDto(Assignment assignment) {
        return assignmentMapper.convertToDTO(assignment);
    }
}
