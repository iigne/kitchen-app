package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.dto.request.RequestRecipeDTO;
import com.kitchenapp.kitchenappapi.dto.response.ResponseRecipeDTO;
import com.kitchenapp.kitchenappapi.model.Recipe;
import com.kitchenapp.kitchenappapi.model.RecipeIngredient;
import com.kitchenapp.kitchenappapi.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeMapper {

    public static Recipe toEntity(RequestRecipeDTO requestRecipeDTO, User author) {
        return Recipe.builder()
                .title(requestRecipeDTO.getTitle())
                .imageLink(requestRecipeDTO.getImageLink())
                .method(requestRecipeDTO.getMethod())
                .author(author)
                .build();
    }

    public static Recipe toEntity(RequestRecipeDTO dto, Recipe recipe, Set<RecipeIngredient> recipeIngredients) {
        return Recipe.builder()
                .id(recipe.getId())
                .author(recipe.getAuthor())
                .title(dto.getTitle())
                .imageLink(dto.getImageLink())
                .method(dto.getMethod())
                .recipeIngredients(recipeIngredients)
                .build();
    }

    public static ResponseRecipeDTO toDTO(Recipe entity) {
        return ResponseRecipeDTO.builder()
                .id(entity.getId())
                .authorId(entity.getAuthor().getId())
                .imageLink(entity.getImageLink())
                .method(entity.getMethod())
                .title(entity.getTitle())
                .recipeIngredients(RecipeIngredientMapper.toDTOs(entity.getRecipeIngredients()))
                .build();
    }

    public static List<ResponseRecipeDTO> toDTOs(List<Recipe> entities) {
        return entities.stream().map(RecipeMapper::toDTO).collect(Collectors.toList());
    }

}
