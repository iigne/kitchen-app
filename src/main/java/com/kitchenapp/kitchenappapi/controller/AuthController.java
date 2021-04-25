package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.dto.user.JwtResponseDTO;
import com.kitchenapp.kitchenappapi.dto.user.LoginDTO;
import com.kitchenapp.kitchenappapi.dto.user.RegisterDTO;
import com.kitchenapp.kitchenappapi.model.user.User;
import com.kitchenapp.kitchenappapi.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO registerRequest) {
        User user = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user.toString());
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginDTO loginRequest) {
        JwtResponseDTO loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
