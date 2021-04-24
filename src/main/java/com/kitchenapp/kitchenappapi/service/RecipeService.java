package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.recipe.RequestRecipeDTO;
import com.kitchenapp.kitchenappapi.dto.recipe.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.dto.recipe.ResponseRecipeDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.mapper.RecipeIngredientMapper;
import com.kitchenapp.kitchenappapi.mapper.RecipeMapper;
import com.kitchenapp.kitchenappapi.model.*;
import com.kitchenapp.kitchenappapi.repository.RecipeIngredientRepository;
import com.kitchenapp.kitchenappapi.repository.RecipeRepository;
import com.kitchenapp.kitchenappapi.repository.projection.RecipeUserIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    private final IngredientService ingredientService;
    private final MeasurementService measurementService;
    private final UserService userService;

    public List<ResponseRecipeDTO> getAllWithQuantities(final int userId) {
        List<Recipe> recipes = recipeRepository.findAll();
        List<RecipeUserIngredient> recipeUserIngredients = recipeIngredientRepository.fetchIngredientQuantitiesForAllRecipesByUserId(userId);
        return RecipeMapper.toDTOs(recipes, recipeUserIngredients, userId);
    }

    public List<ResponseRecipeDTO> getAllCreatedByUser(final int userId) {
        List<Recipe> recipes = recipeRepository.findAllByAuthorId(userId);
        List<RecipeUserIngredient> recipeUserIngredients =
                recipeIngredientRepository.fetchIngredientQuantitiesByUserIdAndRecipeIdsIn(userId,
                        recipes.stream().map(Recipe::getId).collect(Collectors.toList()));
        return RecipeMapper.toDTOs(recipes, recipeUserIngredients, userId);
    }

    public List<ResponseRecipeDTO> getAllLikedByUser(final int userId) {
        Set<Recipe> userRecipes = userService.findByIdOrThrow(userId).getUserRecipes();
        List<RecipeUserIngredient> recipeUserIngredients = recipeIngredientRepository
                .fetchIngredientQuantitiesByUserIdAndRecipeIdsIn(userId, userRecipes.stream().map(Recipe::getId).collect(Collectors.toList()));
        return RecipeMapper.toDTOs(new ArrayList<>(userRecipes), recipeUserIngredients, userId);
    }

    public Recipe getByIdOrThrow(int recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("recipeId %s not found", recipeId)));
    }

    public ResponseRecipeDTO create(RequestRecipeDTO requestRecipeDTO, final int userId) {

        User user = userService.findByIdOrThrow(userId);

        Recipe recipe = recipeRepository.save(RecipeMapper.toEntity(requestRecipeDTO, user));

        Set<RecipeIngredient> recipeIngredients = getRecipeIngredientsFromDTO(requestRecipeDTO.getIngredients(), recipe);

        recipe.setRecipeIngredients(recipeIngredients);
        Recipe savedRecipe = recipeRepository.save(recipe);

        List<RecipeUserIngredient> recipeUserIngredients = recipeIngredientRepository.fetchIngredientQuantitiesByUserIdAndRecipeId(userId, savedRecipe.getId());
        return RecipeMapper.toDTO(savedRecipe, recipeUserIngredients, userId);
    }

    public ResponseRecipeDTO update(RequestRecipeDTO requestRecipeDTO, final int userId) {
        Recipe recipeToEdit = getByIdOrThrow(requestRecipeDTO.getId());
        final int recipeAuthorId = recipeToEdit.getAuthor().getId();

        if(userId != recipeAuthorId) {
            throw new UnsupportedOperationException(String.format("user %s is not allowed to alter recipe created by user %s", userId, recipeAuthorId));
        }

        Set<RecipeIngredient> recipeIngredients = handleUpdateIngredients(requestRecipeDTO.getIngredients(), recipeToEdit);
        Recipe updatedRecipe = RecipeMapper.toEntity(requestRecipeDTO, recipeToEdit, recipeIngredients);
        Recipe savedRecipe = recipeRepository.save(updatedRecipe);
        List<RecipeUserIngredient> recipeUserIngredients = recipeIngredientRepository.fetchIngredientQuantitiesByUserIdAndRecipeId(userId, savedRecipe.getId());
        return RecipeMapper.toDTO(savedRecipe, recipeUserIngredients, userId);
    }

    private Set<RecipeIngredient> getRecipeIngredientsFromDTO(List<IngredientQuantityDTO> recipeIngredientDTOs, Recipe recipe) {
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();

        for(IngredientQuantityDTO dto : recipeIngredientDTOs) {
            RecipeIngredient ingredient = createNew(dto, recipe);
            recipeIngredients.add(ingredient);
        }
        return recipeIngredients;
    }

    private Set<RecipeIngredient> handleUpdateIngredients(List<IngredientQuantityDTO> updatedIngredients, Recipe recipe) {
        Set<RecipeIngredient> savedIngredients = recipe.getRecipeIngredients();

        Map<Integer, IngredientQuantityDTO> updatedMap = updatedIngredients.stream().collect(Collectors.toMap(IngredientQuantityDTO::getIngredientId, Function.identity()));
        Map<Integer, RecipeIngredient> savedMap = savedIngredients.stream().collect(Collectors.toMap(i -> i.getIngredient().getId(), Function.identity()));

        for(Map.Entry<Integer, IngredientQuantityDTO> updated : updatedMap.entrySet()) {
            RecipeIngredient savedValue = savedMap.get(updated.getKey());
            IngredientQuantityDTO updatedValue = updated.getValue();

            if (savedValue == null) {
                savedIngredients.add(createNew(updatedValue, recipe));
            } else {
                Measurement savedMeasurement = savedValue.getCustomMeasurement();
                if(updatedAndSavedDiffer(savedValue, updatedValue)) {
                    Measurement updatedMeasurement = savedMeasurement.getId() != updatedValue.getMeasurementId() ? measurementService.findByIdOrThrow(updatedValue.getMeasurementId()) : savedMeasurement;
                    savedIngredients.remove(savedValue);
                    savedIngredients.add(RecipeIngredientMapper.toEntity(savedValue, updatedValue.getQuantity(), updatedMeasurement));
                }
            }
        }
        //handle remove
        for(Map.Entry<Integer, RecipeIngredient> saved : savedMap.entrySet()) {
            RecipeIngredient savedValue = saved.getValue();
            IngredientQuantityDTO updatedValue = updatedMap.get(saved.getKey());
            if(updatedValue == null) {
                savedIngredients.remove(savedValue);
            }
        }
        return savedIngredients;
    }

    private boolean updatedAndSavedDiffer(RecipeIngredient saved, IngredientQuantityDTO updated) {
        if(saved.getCustomMeasurement().getId() != updated.getMeasurementId()) {
            return true;
        } else {
            if(saved.getCustomMeasurement().isMetric()) {
                return !(updated.getQuantity() == saved.getMetricQuantity());
            } else {
                return !(MeasurementConverter.toMetric(updated.getQuantity(), saved.getCustomMeasurement()) == saved.getMetricQuantity());
            }
        }
    }

    private RecipeIngredient createNew(IngredientQuantityDTO dto, Recipe recipe) {
        Ingredient ingredient = ingredientService.findByIdOrThrow(dto.getIngredientId());
        Measurement measurement = measurementService.findByIdOrThrow(dto.getMeasurementId());
        return RecipeIngredientMapper.toEntity(dto.getQuantity(), ingredient, measurement, recipe);
    }

    public void delete(final int recipeId, final int userId) {
        Recipe recipe = getByIdOrThrow(recipeId);
        final int recipeAuthorId = recipe.getAuthor().getId();
        if(userId != recipeAuthorId) {
            throw new UnsupportedOperationException(String.format("user %s is not allowed to alter recipe created by user %s", userId, recipeAuthorId));
        }
        recipeRepository.delete(recipe);
    }

    public boolean removeOrAddFromUserRecipes(final int recipeId, final int userId) {
        User user = userService.findByIdOrThrow(userId);
        Recipe recipe = getByIdOrThrow(recipeId);

        boolean isRecipeLiked = recipe.getUsers().stream().anyMatch(u -> u.getId() == userId);
        if(isRecipeLiked) {
            recipe.getUsers().removeIf(u -> u.getId() == userId);
        } else {
            recipe.getUsers().add(user);
        }

        return recipeRepository.save(recipe).getUsers().stream().anyMatch(u -> u.getId() == userId);
    }
}
