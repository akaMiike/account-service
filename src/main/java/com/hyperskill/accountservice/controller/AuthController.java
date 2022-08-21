package com.hyperskill.accountservice.controller;

import com.hyperskill.accountservice.dto.NewPasswordCreateDTO;
import com.hyperskill.accountservice.dto.UserDataCreateDTO;
import com.hyperskill.accountservice.dto.UserDataReturnDTO;
import com.hyperskill.accountservice.dto.UserDataStatusDTO;
import com.hyperskill.accountservice.entity.BreachedPasswords;
import com.hyperskill.accountservice.entity.User;
import com.hyperskill.accountservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

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
        else if(BreachedPasswords.PASSWORDS.contains(userData.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
        else if(userData.getPassword().length() < 12){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password length must be 12 chars minimum!");
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

    @PostMapping("/changepass")
    public ResponseEntity<UserDataStatusDTO> changePassword(@RequestBody NewPasswordCreateDTO newPasswordData,
                                                            @AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findByEmail(userDetails.getUsername().toLowerCase()).get();
        String newPassword = newPasswordData.getNewPassword();
        String oldPassword = user.getPassword();

        if(encoder.matches(newPassword, oldPassword)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }
        else if(BreachedPasswords.PASSWORDS.contains(newPassword)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
        else if(newPassword.length() < 12){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password length must be 12 chars minimum!");
        }

        user.setPassword(encoder.encode(newPassword));
        userService.save(user);

        return ResponseEntity.ok(
                new UserDataStatusDTO(
                        user.getEmail(),
                        "The password has been updated successfully"
                )
        );
    }

}
