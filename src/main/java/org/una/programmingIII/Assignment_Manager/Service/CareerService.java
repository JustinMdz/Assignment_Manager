package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.CareerDto;

import java.util.Optional;


public interface CareerService {

    Optional<CareerDto> getById(Long id);

    CareerDto create(CareerDto careerDto);

    CareerDto update(CareerDto careerDto, Long id);

    void delete(Long id);
}
