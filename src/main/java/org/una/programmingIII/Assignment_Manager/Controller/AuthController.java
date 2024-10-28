package org.una.programmingIII.Assignment_Manager.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.Input.LoginInput;
import org.una.programmingIII.Assignment_Manager.Dto.LoginResponse;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Exception.InvalidCredentialsException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.User;
import org.una.programmingIII.Assignment_Manager.Service.AuthenticationService;
import org.una.programmingIII.Assignment_Manager.Service.JWTService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JWTService jwtService;
    private final GenericMapper<User, UserDto> userMapper;

    @Autowired
    AuthController(AuthenticationService authenticationService, JWTService jwtService, GenericMapperFactory mapperFactory) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.userMapper = mapperFactory.createMapper(User.class, UserDto.class);
    }

    @Operation(summary = "Authenticate user", description = "Authenticates a user by providing their email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Database error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Parameter(description = "Login request containing email and password")
            @RequestBody LoginInput loginRequest) {
        try {
            UserDto userDto = authenticationService.authenticate(loginRequest);
            User user = userMapper.convertToEntity(userDto);
            String accessToken = jwtService.generateAccessToken(userDto);
            String refreshToken = jwtService.generateRefreshToken(userDto);
            System.out.println(userDto);
            return ResponseEntity.ok(new LoginResponse(userDto, accessToken, refreshToken));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (InvalidCredentialsException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Server Error", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(
            @Parameter(description = "Refresh token")
            @RequestParam String refreshToken) {
        try {
            String newAccessToken = jwtService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(newAccessToken);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new CustomErrorResponse("Invalid refresh token", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = jwtService.isValidToken(token);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    @PostMapping("/sendVerificationEmail")
    public ResponseEntity<?> sendVerificationEmail(@RequestParam String email) {
        try {
            authenticationService.sendVerificationEmail(email);
            return ResponseEntity.ok("Verification email sent successfully.");
        } catch (MessagingException e) {
            return new ResponseEntity<>(new CustomErrorResponse("Error sending verification email", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
