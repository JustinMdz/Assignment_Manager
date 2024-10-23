package org.una.programmingIII.Assignment_Manager.Service;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.Input.LoginInput;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Exception.InvalidCredentialsException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.User;

import java.util.List;


@Service
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncryptionService passwordEncryptionServices;
    private final GenericMapper<User, UserDto> userMapper;
    private final JWTService jwtService;

    @Autowired
    AuthenticationService(UserService userService, PasswordEncryptionService passwordEncryptionService,
                          GenericMapperFactory mapperFactory, JWTService jwtService) {
        this.userService = userService;
        this.userMapper = mapperFactory.createMapper(User.class, UserDto.class);
        this.passwordEncryptionServices = passwordEncryptionService;
        this.jwtService = jwtService;
    }

    public UserDto authenticate(LoginInput loginInput) {
        User user = new User();
        user = userService.findUserByEmail(loginInput.getEmail());
        if (!(passwordEncryptionServices.matches(loginInput.getPassword(), user.getPassword()))) {
             throw new InvalidCredentialsException("Invalid credentials");
        }
        return userMapper.convertToDTO(user);
    }
//
//    public void changePassword(String email, String password, String newPassword) {
//        User user = userService.findByEmail(email);
//        if (!passwordEncryptionServices.matches(password, user.getPassword())) {
//            throw new InvalidCredentialsException("Invalid credentials");
//        }
//
//        user.setPassword(passwordEncryptionServices.encodePassword(newPassword));
//        userService.updateUser(user);
//    }
//
//    public void resetPassword(ResetPasswordRequest input) {
//
//        User user = userService.findByEmail(input.getEmail());
//        if (validateAdminToken(input.getAdminToken()) && validateUsedOneTimeToken(user, input.getAdminToken())) {
//            user.setPassword(passwordEncryptionServices.encodePassword(input.getPassword()));
//            UsedToken usedToken = new UsedToken();
//            usedToken.setToken(input.getAdminToken());
//            usedToken.setUser(user);
//            user.getTokens().add(usedToken);
//            usedTokenService.saveUsedToken(usedToken);
//            userService.updateUser(user);
//
//        } else {
//            throw new JwtException("Invalid or expired token");
//        }
//    }
//
//
//    private boolean validateAdminToken(String token) {
//        return !jwtService.isAccessTokenExpired(token) &&
//                jwtService.validateToken(token) &&
//                jwtService.getRoleFromToken(token).equals("1");
//    }
//
//    private boolean validateUsedOneTimeToken(User user, String token) {
//        List<UsedToken> tokenList = user.getTokens();
//        List<String> tokens = tokenList.stream()
//                .map(UsedToken::getToken)
//                .toList();
//        return !tokens.contains(token);
//    }


}