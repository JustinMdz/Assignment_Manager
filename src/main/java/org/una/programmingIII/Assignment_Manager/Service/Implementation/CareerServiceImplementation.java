package org.una.programmingIII.Assignment_Manager.Service.Implementation;


import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.una.programmingIII.Assignment_Manager.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.Career;
import org.una.programmingIII.Assignment_Manager.Repository.CareerRepository;
import org.una.programmingIII.Assignment_Manager.Service.CareerService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CareerServiceImplementation implements CareerService {

    private final CareerRepository careerRepository;
    private final GenericMapper<Career, CareerDto> careerMapper;

    @Autowired
    public CareerServiceImplementation(GenericMapperFactory mapperFactory, CareerRepository careerRepository) {
        this.careerMapper = mapperFactory.createMapper(Career.class, CareerDto.class);
        this.careerRepository = careerRepository;
    }


    @Override
    public Optional<CareerDto> getById(Long id) {
        return careerRepository.findById(id)
                .map(this::convertToDto);
    }


    @Override
    public CareerDto create(CareerDto careerDto) {
        Career career = careerMapper.convertToEntity(careerDto);
        career = careerRepository.save(career);
        return careerMapper.convertToDTO(career);
    }

    @Override
    public CareerDto update(CareerDto careerDto, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {
        if (careerRepository.existsById(id)) {
            careerRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Career not found with id " + id);
        }
    }

    private CareerDto convertToDto(Career career) {
        CareerDto careerDto = careerMapper.convertToDTO(career);
        List<Long> usersId = career.getUsers().stream()
                .map(user -> user.getId())
                .collect(Collectors.toList());
        careerDto.setUsersId(usersId);
        
        List<Long> coursesId = career.getCourses().stream()
                .map(course -> course.getId())
                .collect(Collectors.toList());
        careerDto.setCoursesId(coursesId);
        return careerDto;
    }


}