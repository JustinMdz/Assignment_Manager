package org.una.programmingIII.Assignment_Manager.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.una.programmingIII.Assignment_Manager.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();

    Map<String, Object> getUsers(int page, int size, int limit);

    Page<UserDto> getPaseUsers(Pageable pageable);

    Optional<UserDto> getUserByEmail(String email);

    UserDto createUser(UserInput userInput);

    Optional<UserDto> updateUser(Long id, UserInput userInput);

    List<UserDto> getUsersByRole(String role);
    User findUserByEmail(String email);

    void deleteUser(Long id);

}