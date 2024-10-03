package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.University;
import org.una.programmingIII.Assignment_Manager.Repository.UniversityRepository;
import org.una.programmingIII.Assignment_Manager.Service.UniversityService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UniversityServiceImplementation implements UniversityService {

    @Autowired
    private UniversityRepository universityRepository;
    private final GenericMapper<University, UniversityDto> universityMapper;

    public UniversityServiceImplementation(GenericMapperFactory mapperFactory) {
        this.universityMapper = mapperFactory.createMapper(University.class, UniversityDto.class);
    }

    @Override
    public UniversityDto create(UniversityDto universityDto) {
        University university = universityMapper.convertToEntity(universityDto);
        university = universityRepository.save(university);
        return universityMapper.convertToDTO(university);
    }

    @Override
    public List<UniversityDto> getAllUniversities() {
        return universityRepository.findAll().stream()
                .map(universityMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UniversityDto> getById(Long id) {
        return universityRepository.findById(id)
                .map(universityMapper::convertToDTO);
    }

    @Override
    public Optional<UniversityDto> update(Long id, UniversityDto universityDto) {
        return universityRepository.findById(id)
                .map(existingUniversity -> {
                    University updatedUniversity = universityMapper.convertToEntity(universityDto);
                    updatedUniversity.setId(id);
                    University savedUniversity = universityRepository.save(updatedUniversity);
                    return Optional.of(universityMapper.convertToDTO(savedUniversity));
                })
                .orElseThrow(() -> new EntityNotFoundException("University not found with id " + id));
    }

    @Override
    public void delete(Long id) {
        if (universityRepository.existsById(id)) {
            universityRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("University not found with id " + id);
        }
    }

    @Override
    public Map<String, Object> getUniversities(int page, int size, int limit) {
        Page<University> universityPage = universityRepository.findAll(PageRequest.of(page, size));
        universityPage.forEach(university -> {
            university.setFaculties(limitListOrDefault(university.getFaculties(), limit));
        });

        Map<String, Object> response = new HashMap<>();
        response.put("universities", universityPage.map(this::convertToDto).getContent());
        response.put("totalPages", universityPage.getTotalPages());
        response.put("totalElements", universityPage.getTotalElements());
        return response;
    }


    @Override
    public Page<UniversityDto> getPageUniversities(Pageable pageable) {
        Page<University> universityPage = universityRepository.findAll(pageable);
        return universityPage.map(universityMapper::convertToDTO);
    }


    private <T> List<T> limitListOrDefault(List<T> list, int limit) {
        return list == null ? new ArrayList<>() : limitList(list, limit);
    }

    private <T> List<T> limitList(List<T> list, int limit) {
        return list.stream().limit(limit).collect(Collectors.toList());
    }

    private UniversityDto convertToDto(University university) {
        return universityMapper.convertToDTO(university);
    }

}
