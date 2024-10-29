package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.una.programmingIII.Assignment_Manager.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager.Exception.BlankInputException;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Exception.DuplicateEmailException;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Service.EmailService;
import org.una.programmingIII.Assignment_Manager.Service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Optional<UserDto> updatedUser = userService.findById(id);
            return updatedUser.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("getMap")
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "5") int limit) {
        Map<String, Object> response = userService.getUsers(page, size, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("getPageable")
    public Page<UserDto> getUsers(Pageable pageable) {
        return userService.getPaseUsers(pageable);
    }


    @GetMapping("/findByEmail")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        try {
            Optional<UserDto> userDTO = userService.getUserByEmail(email);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserInput userInput) {
        try {
            UserDto createdUser = userService.createUser(userInput);
            emailService.enviarCorreoActivacion(createdUser.getEmail(), "Subjet", createdUser.getId());
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

        }catch (DuplicateEmailException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value()), HttpStatus.CONFLICT);
        }
        catch (BlankInputException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserInput userInput) {
        try {
            Optional<UserDto> updatedUser = userService.updateUser(id, userInput);
            return updatedUser.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ElementNotFoundException ex) {
            return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/activate/{id}")
    public UserDto activarUsuario(@PathVariable Long id) {
        return userService.activateUser(id);
    }

}
