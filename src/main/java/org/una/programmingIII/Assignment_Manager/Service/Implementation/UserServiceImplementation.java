//package org.una.programingIII.loans.Service.Implementation;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.una.programingIII.loans.Dto.UserDTO;
//import org.una.programingIII.loans.Exception.ElementNotFoundException;
//import org.una.programingIII.loans.Model.User;
//import org.una.programingIII.loans.Repository.UserRepository;
//import org.una.programingIII.loans.Mapper.GenericMapper;
//import org.una.programingIII.loans.Mapper.GenericMapperFactory;
//import org.una.programingIII.loans.Service.PasswordEncryptionService;
//import org.una.programingIII.loans.Service.UserService;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class UserServiceImplementation implements UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncryptionService passwordEncryptionService;
//
//    private final GenericMapper<User, UserDTO> userMapper;
//
//    public UserServiceImplementation(GenericMapperFactory mapperFactory) {
//        this.userMapper = mapperFactory.createMapper(User.class, UserDTO.class);
//    }
//
//    @Override
//    public List<UserDTO> getAllUsers() {//revisaar si hay que hacer paginacion aca
//        return userRepository.findAll().stream()
//                .map(userMapper::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Optional<UserDTO> getUserByEmail(String email) {
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            throw new ElementNotFoundException("User with email " + email + " not found");
//        }
//        return Optional.of(userMapper.convertToDTO(user));
//    }
//
//    @Override
//    public UserDTO createUser(UserDTO userDTO) {
//        User user = userMapper.convertToEntity(userDTO);
//        user.setPassword(passwordEncryptionService.encodePassword(userDTO.getPassword()));
//        User savedUser = userRepository.save(user);
//        savedUser.setPassword(null);
//        return userMapper.convertToDTO(savedUser);
//    }
//
//    @Override
//    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
//        return userRepository.findById(id)
//                .map(existingUser -> {
//                    User updatedUser = userMapper.convertToEntity(userDTO);
//                    updatedUser.setId(id);
//                    updatedUser.setCreatedAt(existingUser.getCreatedAt());
//
//                    User savedUser = userRepository.save(updatedUser);
//                    return Optional.of(userMapper.convertToDTO(savedUser));
//                })
//                .orElseThrow(() -> new ElementNotFoundException("User with ID " + id + " not found"));
//    }
//
//
//    @Override
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }
//
//    @Override
//    public User findByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
//
//    @Override
//    public void updateUser(User user) {
//        userRepository.save(user);
//    }
//}
