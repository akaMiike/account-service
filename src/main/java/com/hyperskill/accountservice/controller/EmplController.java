package com.hyperskill.accountservice.controller;

import com.hyperskill.accountservice.dto.UserDataReturnDTO;
import com.hyperskill.accountservice.entity.BreachedPasswords;
import com.hyperskill.accountservice.entity.User;
import com.hyperskill.accountservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("api/empl")
public class EmplController {

    @Autowired
    private UserService userService;

    @GetMapping("/payment")
    public ResponseEntity<UserDataReturnDTO> getDataUser(@AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        String userPassword = userDetails.getPassword();
        Optional<User> existingUser = userService.findByEmail(userEmail);

        if(existingUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
        }
        else if(BreachedPasswords.PASSWORDS.contains(userPassword)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
        else if(userPassword.length() < 12){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password length must be 12 chars minimum!");
        }

        User user = existingUser.get();

        return ResponseEntity.ok(
                new UserDataReturnDTO(
                        user.getId(),
                        user.getName(),
                        user.getLastName(),
                        user.getEmail()
                )
        );
    }
}
