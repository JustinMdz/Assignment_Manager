package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.DepartmentDto;
import org.una.programmingIII.Assignment_Manager.Dto.FacultyDto;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.Faculty;
import org.una.programmingIII.Assignment_Manager.Repository.FacultyRepository;
import org.una.programmingIII.Assignment_Manager.Service.FacultyService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImplementation implements FacultyService {
    @Autowired
    private FacultyRepository facultyRepository;
    private final GenericMapper<Faculty, FacultyDto> facultyMapper;

    public FacultyServiceImplementation(GenericMapperFactory mapperFactory) {
        this.facultyMapper = mapperFactory.createMapper(Faculty.class, FacultyDto.class);
    }

    @Override
    public FacultyDto create(FacultyDto facultyDto) {
        Faculty faculty = facultyMapper.convertToEntity(facultyDto);
        faculty = facultyRepository.save(faculty);
        return facultyMapper.convertToDTO(faculty);
    }

    @Override
    public List<FacultyDto> getAllFaculties() {
        return facultyRepository.findAll().stream()
                .map(facultyMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FacultyDto> getById(Long id) {
        return facultyRepository.findById(id)
                .map(facultyMapper::convertToDTO);
    }

    @Override
    public Optional<FacultyDto> update(Long id, FacultyDto facultyDto) {
        return facultyRepository.findById(id)
                .map(existingFaculty -> {
                    Faculty updatedFaculty = facultyMapper.convertToEntity(facultyDto);
                    updatedFaculty.setId(id);
                    Faculty savedFaculty = facultyRepository.save(updatedFaculty);
                    return Optional.of(facultyMapper.convertToDTO(savedFaculty));
                })
                .orElseThrow(() -> new EntityNotFoundException("Faculty not found with id " + id));
    }

    @Override
    public void delete(Long id) {
        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Faculty not found with id " + id);
        }
    }
}
