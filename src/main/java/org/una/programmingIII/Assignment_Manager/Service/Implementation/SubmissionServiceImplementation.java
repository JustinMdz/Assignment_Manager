package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Repository.SubmissionRepository;
import org.una.programmingIII.Assignment_Manager.Service.SubmissionService;
@Service
@AllArgsConstructor
public class SubmissionServiceImplementation implements SubmissionService {
    private final SubmissionRepository submissionRepository;
}
