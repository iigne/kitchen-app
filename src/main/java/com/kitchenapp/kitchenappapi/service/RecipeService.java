package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.RecipeDTO;
import com.kitchenapp.kitchenappapi.dto.RecipeIngredientDTO;
import com.kitchenapp.kitchenappapi.mapper.RecipeIngredientMapper;
import com.kitchenapp.kitchenappapi.mapper.RecipeMapper;
import com.kitchenapp.kitchenappapi.model.*;
import com.kitchenapp.kitchenappapi.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientService ingredientService;
    private final MeasurementService measurementService;
    private final UserService userService;

    public List<Recipe> getAll() {
        return recipeRepository.findAll();
    }

    public Set<Recipe> getAllByUser(final int userId) {
        return userService.findByIdOrThrow(userId).getUserRecipes();
    }

    public Recipe getByIdOrThrow(int recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("recipeId %s not found", recipeId)));
    }

    public Recipe create(RecipeDTO recipeDTO, final int userId) {

        Set<RecipeIngredient> recipeIngredients = new HashSet<>();
        Recipe recipe = recipeRepository.save(RecipeMapper.toEntity(recipeDTO));


        for(RecipeIngredientDTO dto : recipeDTO.getRecipeIngredients()) {
            Ingredient ingredient = ingredientService.findByIdOrThrow(dto.getIngredientId());
            Measurement measurement = measurementService.findByIdOrThrow(dto.getMeasurementId());
            RecipeIngredient recipeIngredient = RecipeIngredientMapper.toEntity(dto.getQuantity(), ingredient, measurement, recipe);
            recipeIngredients.add(recipeIngredient);
        }

        User user = userService.findByIdOrThrow(userId);
        recipe.setRecipeIngredients(recipeIngredients);
        //user's created recipe shows up in their library
        userService.addRecipeToUser(user, recipe);

        return recipeRepository.save(recipe);
    }

}
