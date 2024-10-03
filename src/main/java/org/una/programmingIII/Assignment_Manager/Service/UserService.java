package org.una.programmingIII.Assignment_Manager.Service;

import org.una.programmingIII.Assignment_Manager.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();

    Optional<UserDto> getUserByEmail(String email);

    UserDto createUser(UserInput userInput);

    Optional<UserDto> updateUser(Long id, UserInput userInput);

    void deleteUser(Long id);

}