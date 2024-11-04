package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.AnswerAIDto;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.AnswerAI;
import org.una.programmingIII.Assignment_Manager.Repository.AnswerAIRepository;
import org.una.programmingIII.Assignment_Manager.Service.AnswerAIService;

import java.util.List;

@Service
public class AnswerAIServiceImplementation implements AnswerAIService {
    private final AnswerAIRepository answerAIRepository;
    private final GenericMapper<AnswerAI, AnswerAIDto> genericMapper;

    @Autowired
    public AnswerAIServiceImplementation(AnswerAIRepository answerAIRepository, GenericMapperFactory mapperFactory) {
        this.answerAIRepository = answerAIRepository;
        this.genericMapper = mapperFactory.createMapper(AnswerAI.class, AnswerAIDto.class);
    }

    @Override
    public List<AnswerAIDto> findAll() {
        List<AnswerAI> answerAIList = answerAIRepository.findAll();
        return answerAIList.stream().map(genericMapper::convertToDTO).toList();
    }

    @Override
    public AnswerAIDto getById(Long id) {
        return answerAIRepository.findById(id).map(genericMapper::convertToDTO).orElse(null);
    }
}
