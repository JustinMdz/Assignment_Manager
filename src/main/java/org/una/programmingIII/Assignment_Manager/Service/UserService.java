package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();

    Optional<UserDto> getUserByEmail(String email);

    UserDto createUser(UserDto userDTO);

    Optional<UserDto> updateUser(Long id, UserDto userDTO);

    void deleteUser(Long id);

    User findByEmail(String email);

    void updateUser(User user);

}