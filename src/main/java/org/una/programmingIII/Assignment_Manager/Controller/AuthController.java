package org.una.programmingIII.Assignment_Manager.Controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;


@Controller
public class AuthController {
//    private final AuthenticationService authenticationService;
//    private final RefreshTokenService refreshTokenService;
//    private final JWTService jwtService;
//    private final GenericMapper<User, UserDto> userMapper;
//    private final UserService userService;
//
//
//    @Autowired
//    AuthController(AuthenticationService authenticationService, RefreshTokenService refreshTokenService, JWTService jwtService, GenericMapperFactory mapperFactory, UserService userService) {
//        this.authenticationService = authenticationService;
//        this.refreshTokenService = refreshTokenService;
//        this.jwtService = jwtService;
//        this.userMapper = mapperFactory.createMapper(User.class, UserDto.class);
//        this.userService = userService;
//    }
//
//    @MutationMapping
//    public LoginResponse login(@Argument LogInInput input) {
//        try {
//            UserDto userDto = authenticationService.authenticate(input.getEmail(), input.getPassword());
//            User user = userMapper.convertToEntity(userDto);
//            String accessToken = jwtService.generateAccessToken(user);
//            String refreshToken = jwtService.generateRefreshToken(user);
//            return new LoginResponse(userDto, accessToken, refreshToken);
//        } catch (Exception e) {
//            throw new BadCredentialsException("Invalid credentials");
//        }
//    }
//
//    @MutationMapping
//    public RefreshTokenDto refreshToken(@Argument String refreshToken) {
//        try {
//            String newAccessToken = refreshTokenService.refreshAccessToken(refreshToken);
//            return new RefreshTokenDto(newAccessToken);
//        } catch (AuthenticationException e) {
//            throw new BadCredentialsException("Invalid refresh token");
//        }
//    }
//
//    @MutationMapping
//    public void changePassword(@Argument ChangePasswordInput input) {
//        try {
//            authenticationService.changePassword(input.getEmail(), input.getPassword(), input.getNewPassword());
//        } catch (AuthenticationException e) {
//            throw new BadCredentialsException("Invalid credentials");
//        }
//    }
//
//    @MutationMapping
//    public String initiateRecoverPassword(@Argument String email) {
//        try {
//            return authenticationService.initiatePasswordRecovery(email);
//        } catch (AuthenticationException e) {
//            throw new BadCredentialsException("Invalid credentials");
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @MutationMapping
//    public String recoverPassword(@Argument RecoverPasswordInput input) {
//        try {
//            return authenticationService.completePasswordRecovery(input);
//        } catch (AuthenticationException e) {
//            throw new BadCredentialsException("Invalid credentials");
//        }
//    }
//    @MutationMapping
//    public UserDto createUser(@Argument UserInput input) {
//        try {
//            return userService.createUser(input);
//        } catch (Exception e) {
//            throw new CustomException("Could not create user"+ e.getMessage(), e);
//        }
//    }
}
