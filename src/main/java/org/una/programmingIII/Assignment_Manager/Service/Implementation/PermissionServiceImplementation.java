package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager.Dto.PermissionDto;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.Permission;
import org.una.programmingIII.Assignment_Manager.Model.PermissionType;
import org.una.programmingIII.Assignment_Manager.Model.User;
import org.una.programmingIII.Assignment_Manager.Repository.PermissionRepository;
import org.una.programmingIII.Assignment_Manager.Repository.UserRepository;
import org.una.programmingIII.Assignment_Manager.Service.PasswordEncryptionService;
import org.una.programmingIII.Assignment_Manager.Service.PermissionService;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImplementation implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final GenericMapper<Permission, PermissionDto> permissionMapper;

    public PermissionServiceImplementation(GenericMapperFactory genericMapperFactory, PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = genericMapperFactory.createMapper(Permission.class, PermissionDto.class);
    }

    @Override
    public Optional<PermissionDto> findById(Long id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::convertToDTO);
    }

    @Override
    public List<PermissionDto> getAllPermission() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::convertToDTO)
                .toList();
    }

//    @Override
//    public Optional<PermissionDto> create(PermissionDto permissionDto) {
//        return Optional.of(permissionMapper.convertToEntity(permissionDto))
//                .map(permissionRepository::save)
//                .map(permissionMapper::convertToDTO);
//    }}

}