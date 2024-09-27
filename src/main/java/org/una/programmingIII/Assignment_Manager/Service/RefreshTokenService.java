//package org.una.programingIII.loans.Service;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.stereotype.Service;
//import org.una.programingIII.loans.Model.User;
//
//@Service
//public class RefreshTokenService {
//    private final JWTService jwtService;
//    private final UserService userService;
//
//    @Autowired
//    public RefreshTokenService(JWTService jwtService, @Lazy UserService userService) {
//        this.jwtService = jwtService;
//        this.userService = userService;
//    }
//
//    public String refreshAccessToken(String refreshToken) {
//        if (jwtService.validateToken(refreshToken)) {
//            String email = jwtService.getEmailFromToken(refreshToken);
//            User user = userService.findByEmail(email);
//            return jwtService.generateAccessToken(user);
//        } else {
//            throw new BadCredentialsException("Invalid refresh token");
//        }
//    }
//}