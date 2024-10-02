package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.User;
import org.una.programmingIII.Assignment_Manager.Repository.UserRepository;
import org.una.programmingIII.Assignment_Manager.Service.PasswordEncryptionService;
import org.una.programmingIII.Assignment_Manager.Service.UserService;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncryptionService passwordEncryptionService;

    private final GenericMapper<User, UserDto> userMapper;
    private final GenericMapper<User, UserInput> userInputMapper;

    public UserServiceImplementation(GenericMapperFactory mapperFactory) {
        this.userMapper = mapperFactory.createMapper(User.class, UserDto.class);
        this.userInputMapper = mapperFactory.createMapper(User.class, UserInput.class);
    }

    @Override
    public List<UserDto> getAllUsers() {//revisaar si hay que hacer paginacion aca
        return userRepository.findAll().stream()
                .map(userMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ElementNotFoundException("User with email " + email + " not found");
        }
        return Optional.of(userMapper.convertToDTO(user));
    }

    @Override
    public UserDto createUser(UserInput userInput) {
        if (checkImportantSpaces(userInput)) {
            throw new BlankInputException("Important spaces have blank inputs or are not included.");
        }
        User user = userInputMapper.convertToEntity(userInput);
        user.setPassword(passwordEncryptionService.encodePassword(userInput.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setLastUpdate(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return userMapper.convertToDTO(savedUser);
    }

    @Override
    public Optional<UserDto> updateUser(Long id, UserDto userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    User updatedUser = userMapper.convertToEntity(userDTO);
                    updatedUser.setId(id);
                    updatedUser.setCreatedAt(existingUser.getCreatedAt());

                    User savedUser = userRepository.save(updatedUser);
                    return Optional.of(userMapper.convertToDTO(savedUser));
                })
                .orElseThrow(() -> new ElementNotFoundException("User with ID " + id + " not found"));
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    private boolean checkImportantSpaces(UserInput userInput) {
        return userInput.getName().isBlank() || userInput.getEmail().isBlank() || userInput.getPassword().isBlank();
    }
}
