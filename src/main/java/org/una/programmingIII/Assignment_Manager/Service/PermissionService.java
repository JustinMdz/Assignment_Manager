package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.PermissionDto;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Model.Permission;
import org.una.programmingIII.Assignment_Manager.Model.User;

import java.util.List;
import java.util.Optional;

public interface PermissionService {

    List<PermissionDto> getAllPermission();

    Optional<PermissionDto> findById(Long id);


}
