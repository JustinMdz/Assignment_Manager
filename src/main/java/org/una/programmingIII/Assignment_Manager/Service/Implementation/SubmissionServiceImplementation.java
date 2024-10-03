package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.una.programmingIII.Assignment_Manager.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.Submission;
import org.una.programmingIII.Assignment_Manager.Model.University;
import org.una.programmingIII.Assignment_Manager.Repository.SubmissionRepository;
import org.una.programmingIII.Assignment_Manager.Service.SubmissionService;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubmissionServiceImplementation implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final GenericMapper<Submission, SubmissionDto> submissionMapper;

    @Autowired
    public SubmissionServiceImplementation(SubmissionRepository repository, GenericMapperFactory mapperFactory) {
        this.submissionRepository = repository;
        this.submissionMapper = mapperFactory.createMapper(Submission.class, SubmissionDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Submission submission = submissionRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("FeedingSchedule not found with id: " + id));
        submissionRepository.deleteById(submission.getId());
    }

    @Override
    public Optional<SubmissionDto> findById(Long id) {
        return Optional.of(submissionMapper.convertToDTO(submissionRepository.findById(id).orElseThrow(()
                -> new ElementNotFoundException("FeedingSchedule not found with id: " + id))));
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
    public Map<String, Object> getAllSubmissions(int page, int size) {
        return null;
    }
}
