package com.kitchenapp.kitchenappapi.service.user;

import com.kitchenapp.kitchenappapi.model.user.User;
import com.kitchenapp.kitchenappapi.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByIdOrThrow(final int id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("userId %s not found", id)));
    }

    public boolean isUsernameOrEmailTaken(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email).isPresent();
    }

    public User save(String username, String email, String encodedPassword) {
        User user = User.builder()
                .email(email)
                .username(username)
                .password(encodedPassword)
                .build();
        return userRepository.save(user);
    }

}
