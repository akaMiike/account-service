package com.hyperskill.accountservice.controller;

import com.hyperskill.accountservice.dto.UserDataCreateDTO;
import com.hyperskill.accountservice.dto.UserDataReturnDTO;
import com.hyperskill.accountservice.entity.User;
import com.hyperskill.accountservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/signup")
    public ResponseEntity<UserDataReturnDTO> register(@RequestBody @Valid UserDataCreateDTO userData){
        Optional<User> existingUser = userService.findByEmail(userData.getEmail().toLowerCase());

        if(existingUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists!");
        }

        User newUser = new User(
                userData.getName(),
                userData.getLastname(),
                userData.getEmail().toLowerCase(),
                encoder.encode(userData.getPassword())
        );

        userService.save(newUser);

        return ResponseEntity.ok(new UserDataReturnDTO(
                newUser.getId(),
                newUser.getName(),
                newUser.getLastName().toLowerCase(),
                newUser.getEmail()
        ));
    }

}
