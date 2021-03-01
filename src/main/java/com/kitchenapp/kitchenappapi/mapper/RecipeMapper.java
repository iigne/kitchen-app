package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.dto.request.RequestRecipeDTO;
import com.kitchenapp.kitchenappapi.dto.response.ResponseRecipeDTO;
import com.kitchenapp.kitchenappapi.model.Recipe;
import com.kitchenapp.kitchenappapi.model.RecipeIngredient;
import com.kitchenapp.kitchenappapi.model.User;
import com.kitchenapp.kitchenappapi.repository.projection.RecipeUserIngredient;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeMapper {

    public static Recipe toEntity(RequestRecipeDTO requestRecipeDTO, User author) {
        return Recipe.builder()
                .title(requestRecipeDTO.getTitle())
                .imageLink(requestRecipeDTO.getImageLink())
                .method(requestRecipeDTO.getMethod())
                .author(author)
                .users(new HashSet<>(Collections.singletonList(author)))
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
                .users(recipe.getUsers())
                .build();
    }

    public static ResponseRecipeDTO toDTO(Recipe entity, List<RecipeUserIngredient> recipeIngredients, int userId) {
        return ResponseRecipeDTO.builder()
                .id(entity.getId())
                .authorId(entity.getAuthor().getId())
                .imageLink(entity.getImageLink())
                .method(entity.getMethod())
                .title(entity.getTitle())
                .liked(entity.getUsers() != null && entity.getUsers().stream().anyMatch(u -> u.getId() == userId))
                .ingredients(RecipeIngredientMapper.toDTOs(recipeIngredients, entity.getRecipeIngredients()))
                .build();
    }

    public static List<ResponseRecipeDTO> toDTOs(List<Recipe> entities, List<RecipeUserIngredient> allIngredients, int userId) {
        Map<Integer, List<RecipeUserIngredient>> ingredientsByRecipe = allIngredients.stream().collect(Collectors.groupingBy(RecipeUserIngredient::getRecipeId));
        return entities.stream().map(e -> toDTO(e, ingredientsByRecipe.get(e.getId()), userId)).collect(Collectors.toList());
    }

}
