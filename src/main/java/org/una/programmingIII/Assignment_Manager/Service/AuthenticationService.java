package org.una.programmingIII.Assignment_Manager.Service;

import jakarta.mail.MessagingException;
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
    private final JWTService jwtService;
    private final EmailService emailService;

    @Autowired
    AuthenticationService(UserService userService, PasswordEncryptionService passwordEncryptionService,
                          GenericMapperFactory mapperFactory, JWTService jwtService, EmailService emailService) {
        this.userService = userService;
        this.userMapper = mapperFactory.createMapper(User.class, UserDto.class);
        this.passwordEncryptionServices = passwordEncryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public UserDto authenticate(LoginInput loginInput) {
        User user = userService.findUserByEmail(loginInput.getEmail());
        if (user == null) {
            throw new ElementNotFoundException("User not found");
        }
        if (!(passwordEncryptionServices.matches(loginInput.getPassword(), user.getPassword()))) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return userMapper.convertToDTO(user);
    }

    public void sendVerificationEmail(String email) throws MessagingException {
        long tokenDuration = 120000;
        String token = jwtService.generateToken(email, tokenDuration);

        emailService.sendSimpleEmail(email,
                "Registar Usuario",
                "Usa este link para poder verficar tu cuenta: " +"http://localhost:8080/auth/"+ token);
    }


}