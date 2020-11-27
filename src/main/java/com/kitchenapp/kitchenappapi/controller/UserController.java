package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.model.User;
import com.kitchenapp.kitchenappapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody User user) {
        if(userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()).isPresent()) {
            log.debug("User {} with email {} already exists", user.getUsername(), user.getEmail());
            return ResponseEntity.badRequest().body("User with this email or username already exists");
        }
        //TODO implement auth
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(user.toString());
    }

}
