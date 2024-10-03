//package org.una.programmingIII.Assignment_Manager.Service;
//
//import jakarta.mail.MessagingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.una.programmingIII.WikiPets.Dto.UserDto;
//import org.una.programmingIII.WikiPets.Exception.NotFoundElementException;
//import org.una.programmingIII.WikiPets.Input.RecoverPasswordInput;
//import org.una.programmingIII.WikiPets.Mapper.GenericMapper;
//import org.una.programmingIII.WikiPets.Mapper.GenericMapperFactory;
//import org.una.programmingIII.WikiPets.Model.User;
//
//import java.time.LocalDate;
//
//@Service
//public class AuthenticationService {
//
//    private final UserService userService;
//    private final JWTService jtwService;
//    private final PasswordEncoder passwordEncoder;
//    private final GenericMapper<User, UserDto> userMapper;
//    private final EmailService emailService;
//
//    @Autowired
//    AuthenticationService(UserService userService, GenericMapperFactory mapperFactory, PasswordEncoder passwordEncoder,JWTService jwtService,EmailService emailService) {
//        this.userService = userService;
//        this.userMapper = mapperFactory.createMapper(User.class, UserDto.class);
//        this.passwordEncoder = passwordEncoder;
//        this.jtwService = jwtService;
//        this.emailService = emailService;
//    }
//
//    public UserDto authenticate(String email, String password) {
//        User user;
//        try {
//            user = userService.findByEmail(email);
//        } catch (AuthenticationException ex) {
//            throw new BadCredentialsException("Invalid credentials");
//        }
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new BadCredentialsException("Invalid credentials");
//        }
//        return userMapper.convertToDTO(user);
//    }
//
//    public String initiatePasswordRecovery(String email) throws MessagingException {
//        User user = userService.findByEmail(email);
//        if(user==null){
//            throw  new NotFoundElementException("No user founded");
//        }
//        String token = jtwService.generateAccessToken(email);
//
//        emailService.sendSimpleEmail(email,
//                "Password recovery",
//                "Use this token to recover your password: " + token);
//
//        return "Email sent successfully";
//    }
//
//    public String completePasswordRecovery(RecoverPasswordInput recoverPasswordInput) {
//        User user = userService.findByEmail(recoverPasswordInput.getEmail());
//        if(user==null){
//            throw new NotFoundElementException("User not found");
//        }
//        System.out.println(!jtwService.isTokenExpired(recoverPasswordInput.getToken()));
//        if(jtwService.isTokenExpired(recoverPasswordInput.getToken())){
//            user.setPassword(passwordEncoder.encode(recoverPasswordInput.getPassword()));
//            user.setLastUpdate(LocalDate.now());
//            userService.updateUser(user);
//            return "Password updated successfully";
//        }
//        return "Invalid token";
//    }
//
//    public void changePassword(String email, String password, String newPassword) {
//        User user = userService.findByEmail(email);
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new BadCredentialsException("Current password is incorrect");
//        }
//        user.setPassword(passwordEncoder.encode(newPassword));
//        userService.updateUser(user);
//    }
//
//
//}
