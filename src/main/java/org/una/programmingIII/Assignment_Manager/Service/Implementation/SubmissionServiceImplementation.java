package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.una.programmingIII.Assignment_Manager.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.Submission;
import org.una.programmingIII.Assignment_Manager.Repository.SubmissionRepository;
import org.una.programmingIII.Assignment_Manager.Service.SubmissionService;

import java.util.Map;

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
    public SubmissionDto findById(Long id) {
        return submissionMapper.convertToDTO(submissionRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("FeedingSchedule not found with id: " + id)));
    }

    @Override
    public SubmissionDto create(Submission submission) {
        return submissionMapper.convertToDTO(submissionRepository.save(submission));
    }

    @Override
    public SubmissionDto update(Submission submission) {
        return submissionMapper.convertToDTO(submissionRepository.save(submission));
    }

    @Override
    public Map<String, Object> getAllSubmissions(int page, int size) {
        return null;
    }
}
