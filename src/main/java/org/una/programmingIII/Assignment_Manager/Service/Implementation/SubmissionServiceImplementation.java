package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.Assignment;
import org.una.programmingIII.Assignment_Manager.Model.Submission;
import org.una.programmingIII.Assignment_Manager.Repository.SubmissionRepository;
import org.una.programmingIII.Assignment_Manager.Service.AssignmentService;
import org.una.programmingIII.Assignment_Manager.Service.SubmissionService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubmissionServiceImplementation implements SubmissionService {

    private final AssignmentService assignmentService;
    private final SubmissionRepository submissionRepository;
    private final GenericMapper<Submission, SubmissionDto> submissionMapper;
    private final GenericMapper<Assignment, AssignmentDto> assignmentMapper;

    @Autowired
    public SubmissionServiceImplementation(AssignmentService assignmentService, SubmissionRepository repository, GenericMapperFactory mapperFactory) {
        this.assignmentService = assignmentService;
        this.submissionRepository = repository;
        this.submissionMapper = mapperFactory.createMapper(Submission.class, SubmissionDto.class);
        this.assignmentMapper = mapperFactory.createMapper(Assignment.class, AssignmentDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Submission submission = submissionRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("FeedingSchedule not found with id: " + id));
        submissionRepository.deleteById(submission.getId());
    }

    @Override
    public Optional<SubmissionDto> findById(Long id) {
        return Optional.of(submissionMapper.convertToDTO(submissionRepository.findById(id).orElseThrow(()
                -> new ElementNotFoundException("Submission not found with id: " + id))));
    }

    @Override
    public SubmissionDto create(SubmissionDto submissionDto) {
        Submission submission = submissionMapper.convertToEntity(submissionDto);
        submission = submissionRepository.save(submission);
        return submissionMapper.convertToDTO(submission);
    }

    @Override
    public Optional<SubmissionDto> update(Long id, SubmissionDto submissionDto) {
        return submissionRepository.findById(id)
                .map(existingSubmission -> {
                    Submission updatedSubmission = submissionMapper.convertToEntity(submissionDto);
                    updatedSubmission.setId(id);
                    Submission savedSubmission = submissionRepository.save(updatedSubmission);
                    return Optional.of(submissionMapper.convertToDTO(savedSubmission));
                })
                .orElseThrow(() -> new EntityNotFoundException("Submission not found with id " + id));
    }

    @Override
    public Map<String, Object> getSubmissions(int page, int size, int limit) {
        Page<Submission> submissionPage = submissionRepository.findAll(PageRequest.of(page, size));
        submissionPage.forEach(submission -> {
            submission.setFiles(limitListOrDefault(submission.getFiles(), limit));
        });

        Map<String, Object> response = new HashMap<>();
        response.put("submissions", submissionPage.map(this::convertToDto).getContent());
        response.put("totalPages", submissionPage.getTotalPages());
        response.put("totalElements", submissionPage.getTotalElements());
        return response;
    }

    @Override
    public List<SubmissionDto> getSubmissionsByAssignmentId(Long assignmentId) {
        List<Submission> submissions = submissionRepository.findByAssignmentId(assignmentId);
        return submissions.stream().map(submissionMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Page<SubmissionDto> getPageSubmissions(Pageable pageable) {
        Page<Submission> submissionPage = submissionRepository.findAll(pageable);
        return submissionPage.map(submissionMapper::convertToDTO);
    }

    @Override
    public Optional<SubmissionDto> getSubmissionByAssignmentIdAndStudentId(Long assignmentId, Long userId) {
        return Optional.of(submissionMapper.convertToDTO(submissionRepository.findByAssignmentIdAndStudentId(assignmentId,userId).orElseThrow(()
                -> new ElementNotFoundException("Submission not found with user id: " + userId + " and assignment id: " + assignmentId))));
    }

    private <T> List<T> limitListOrDefault(List<T> list, int limit) {
        return list == null ? new ArrayList<>() : limitList(list, limit);
    }

    private <T> List<T> limitList(List<T> list, int limit) {
        return list.stream().limit(limit).collect(Collectors.toList());
    }

    private SubmissionDto convertToDto(Submission submission) {
        return submissionMapper.convertToDTO(submission);
    }

}
