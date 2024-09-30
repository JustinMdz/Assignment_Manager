package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.DepartmentDto;
import org.una.programmingIII.Assignment_Manager.Model.Department;
import org.una.programmingIII.Assignment_Manager.Repository.DepartmentRepository;
import org.una.programmingIII.Assignment_Manager.Service.DepartmentService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;

import java.util.Optional;

@Service
public class DepartmentServiceImplementation implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    private final GenericMapper<Department, DepartmentDto> departmentMapper;

    public DepartmentServiceImplementation(GenericMapperFactory mapperFactory) {
        this.departmentMapper = mapperFactory.createMapper(Department.class, DepartmentDto.class);
    }

    @Override
    public DepartmentDto create(DepartmentDto departmentDto) {
        Department department = departmentMapper.convertToEntity(departmentDto);
        department = departmentRepository.save(department);
        return departmentMapper.convertToDTO(department);
    }

    @Override
    public Optional<DepartmentDto> getById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::convertToDTO);
    }

    @Override
    public DepartmentDto update(Long id, DepartmentDto departmentDto) {
        return departmentRepository.findById(id)
                .map(existingDepartment -> {
                    Department updatedDepartment = departmentMapper.convertToEntity(departmentDto);
                    updatedDepartment.setId(id);
                    Department savedDepartment = departmentRepository.save(updatedDepartment);
                    return departmentMapper.convertToDTO(savedDepartment);
                })
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id " + id));
    }

    @Override
    public void delete(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Department not found with id " + id);
        }
    }
}
