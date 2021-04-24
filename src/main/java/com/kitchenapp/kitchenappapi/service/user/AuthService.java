package com.kitchenapp.kitchenappapi.service.user;

import com.kitchenapp.kitchenappapi.config.JwtTokenUtil;
import com.kitchenapp.kitchenappapi.error.UserAlreadyExistsException;
import com.kitchenapp.kitchenappapi.model.user.JwtUserDetails;
import com.kitchenapp.kitchenappapi.model.user.User;
import com.kitchenapp.kitchenappapi.dto.user.JwtResponseDTO;
import com.kitchenapp.kitchenappapi.dto.user.LoginDTO;
import com.kitchenapp.kitchenappapi.dto.user.RegisterDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expiry}")
    private int expiry;

    public User register(final RegisterDTO registerRequest) {
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail();
        if (userService.isUsernameOrEmailTaken(username, email)) {
            throw new UserAlreadyExistsException();
        }
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        return userService.save(username, email, encodedPassword);
    }

    public JwtResponseDTO login(final LoginDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);

        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();

        return new JwtResponseDTO(jwt, userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), expiry);
    }
}
