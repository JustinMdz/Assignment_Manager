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
import org.una.programmingIII.Assignment_Manager.Repository.AssignmentRepository;
import org.una.programmingIII.Assignment_Manager.Service.AssignmentService;

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
        return assignmentMapper.convertToDTO(assignmentRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("FeedingSchedule not found with id: " + id))
        );
    }

    @Override
    public void delete(Long id) {
        assignmentRepository.deleteById(id);
    }

    @Override
    public AssignmentDto create(AssignmentDto assignmentDto) {
        return assignmentMapper.convertToDTO(assignmentRepository.save(assignmentMapper.convertToEntity(assignmentDto)));
    }

    @Override
    public Optional<AssignmentDto> update(Long id, AssignmentDto assignmentDto) {
        return assignmentRepository.findById(id)
                .map(existingUniversity -> {
                    Assignment updatedAssignment = assignmentMapper.convertToEntity(assignmentDto);
                    updatedAssignment.setId(id);
                    Assignment savedAssignment = assignmentRepository.save(updatedAssignment);
                    return Optional.of(assignmentMapper.convertToDTO(savedAssignment));
                })
                .orElseThrow(() -> new EntityNotFoundException("University not found with id " + id));
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
