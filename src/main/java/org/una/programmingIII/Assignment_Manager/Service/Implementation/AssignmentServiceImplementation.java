package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.Assignment;
import org.una.programmingIII.Assignment_Manager.Repository.AssignmentRepository;
import org.una.programmingIII.Assignment_Manager.Service.AssignmentService;
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
        return assignmentMapper.convertToDTO( assignmentRepository.save(assignmentMapper.convertToEntity(assignmentDto)));
    }
    @Override
    public AssignmentDto update(AssignmentDto assignmentDto) {
        return assignmentMapper.convertToDTO( assignmentRepository.save(assignmentMapper.convertToEntity(assignmentDto)));
    }
}
