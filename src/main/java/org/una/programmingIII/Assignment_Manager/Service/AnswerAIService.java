package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.AnswerAIDto;

import java.util.List;

public interface AnswerAIService {
   List<AnswerAIDto> findAll();
    AnswerAIDto getById(Long id);
}
