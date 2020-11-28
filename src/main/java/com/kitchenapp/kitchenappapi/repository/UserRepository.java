package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);

}
