package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.Recipe;
import com.kitchenapp.kitchenappapi.model.User;
import com.kitchenapp.kitchenappapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByIdOrThrow(final int id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("userId %s not found", id)));
    }

    public void addRecipeToUserLibrary(User user, Recipe recipe) {
        user.getUserRecipes().add(recipe);
        userRepository.save(user);
    }
}
