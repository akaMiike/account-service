package com.hyperskill.accountservice.controller;

import com.hyperskill.accountservice.dto.UserDataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @PostMapping("/signup")
    public ResponseEntity<UserDataDTO> register(@RequestBody @Valid UserDataDTO userData){
        return ResponseEntity.ok(
                new UserDataDTO(
                    userData.getName(),
                    userData.getLastname(),
                    userData.getEmail()
                )
        );
    }
}
