package org.una.programmingIII.Assignment_Manager.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Model.User;

import java.util.Optional;

@Service
public class RefreshTokenService {
    private final JWTService jwtService;
    private final UserService userService;

    @Autowired
    public RefreshTokenService(JWTService jwtService, @Lazy UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public String refreshAccessToken(String refreshToken) {
        if (jwtService.validateToken(refreshToken)) {
            String email = jwtService.getEmailFromToken(refreshToken);
            Optional<UserDto> user = userService.getUserByEmail(email);
            return jwtService.generateAccessToken(user);
        } else {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }
}