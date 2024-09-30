package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.User;
import org.una.programmingIII.Assignment_Manager.Repository.UserRepository;
import org.una.programmingIII.Assignment_Manager.Service.PasswordEncryptionService;
import org.una.programmingIII.Assignment_Manager.Service.UserService;


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

    public UserServiceImplementation(GenericMapperFactory mapperFactory) {
        this.userMapper = mapperFactory.createMapper(User.class, UserDto.class);
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
    public UserDto createUser(UserDto userDTO) {
//        User user = userMapper.convertToEntity(userDTO);
//        user.setPassword(passwordEncryptionService.encodePassword(userDTO.getPassword()));
//        User savedUser = userRepository.save(user);
//        savedUser.setPassword(null);
//        return userMapper.convertToDTO(savedUser);
        return null;
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
}
