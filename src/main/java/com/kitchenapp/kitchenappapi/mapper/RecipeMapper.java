package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.dto.RecipeDTO;
import com.kitchenapp.kitchenappapi.model.Recipe;
import com.kitchenapp.kitchenappapi.model.RecipeIngredient;

import java.util.Set;

public class RecipeMapper {

    public static Recipe toEntity(RecipeDTO recipeDTO) {
        return Recipe.builder()
                .title(recipeDTO.getTitle())
                .imageLink(recipeDTO.getImageLink())
                .method(recipeDTO.getMethod())
                .build();
    }

    public static Recipe toEntity(RecipeDTO recipeDTO, Set<RecipeIngredient> recipeIngredients) {
        return Recipe.builder()
                .title(recipeDTO.getTitle())
                .imageLink(recipeDTO.getImageLink())
                .method(recipeDTO.getMethod())
                .recipeIngredients(recipeIngredients)
                .build();
    }

}
