package org.una.programmingIII.Assignment_Manager.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.Input.LoginInput;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Exception.InvalidCredentialsException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.User;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncryptionService passwordEncryptionServices;
    private final GenericMapper<User, UserDto> userMapper;

    @Autowired
    AuthenticationService(UserService userService, PasswordEncryptionService passwordEncryptionService,
                          GenericMapperFactory mapperFactory, JWTService jwtService, EmailService emailService) {
        this.userService = userService;
        this.userMapper = mapperFactory.createMapper(User.class, UserDto.class);
        this.passwordEncryptionServices = passwordEncryptionService;
    }

    public UserDto authenticate(LoginInput loginInput) {
        User user = userService.findUserByEmail(loginInput.getEmail());
        if (user == null || !(user.isActive())) {
            throw new ElementNotFoundException("User not found");
        }

        if (!(passwordEncryptionServices.matches(loginInput.getPassword(), user.getPassword()))) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return userMapper.convertToDTO(user);
    }

}