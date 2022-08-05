package com.hyperskill.accountservice.controller;

import com.hyperskill.accountservice.dto.UserDataReturnDTO;
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
        Optional<User> existingUser = userService.findByEmail(userEmail);

        if(existingUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
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
