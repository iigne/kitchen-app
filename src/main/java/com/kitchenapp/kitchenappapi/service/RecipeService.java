package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.request.RequestRecipeDTO;
import com.kitchenapp.kitchenappapi.dto.request.RequestRecipeIngredientDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.mapper.RecipeIngredientMapper;
import com.kitchenapp.kitchenappapi.mapper.RecipeMapper;
import com.kitchenapp.kitchenappapi.model.*;
import com.kitchenapp.kitchenappapi.repository.RecipeRepository;
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

    private final IngredientService ingredientService;
    private final MeasurementService measurementService;
    private final UserService userService;

    public List<Recipe> getAll() {
        return recipeRepository.findAll();
    }

    public List<Recipe> getAllByUser(final int userId) {
        return new ArrayList<>(userService.findByIdOrThrow(userId).getUserRecipes());
    }

    public Recipe getByIdOrThrow(int recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("recipeId %s not found", recipeId)));
    }

    public Recipe create(RequestRecipeDTO requestRecipeDTO, final int userId) {

        User user = userService.findByIdOrThrow(userId);

        Recipe recipe = recipeRepository.save(RecipeMapper.toEntity(requestRecipeDTO, user));
        userService.addRecipeToUserLibrary(user, recipe);

        Set<RecipeIngredient> recipeIngredients = getRecipeIngredientsFromDTO(requestRecipeDTO.getRecipeIngredients(), recipe);

        recipe.setRecipeIngredients(recipeIngredients);

        return recipeRepository.save(recipe);
    }

    public Recipe update(RequestRecipeDTO requestRecipeDTO, final int userId) {
        Recipe recipeToEdit = getByIdOrThrow(requestRecipeDTO.getId());
        final int recipeAuthorId = recipeToEdit.getAuthor().getId();

        if(userId != recipeAuthorId) {
            throw new UnsupportedOperationException(String.format("user %s is not allowed to alter recipe created by user %s", userId, recipeAuthorId));
        }

        Set<RecipeIngredient> recipeIngredients = handleUpdateIngredients(requestRecipeDTO.getRecipeIngredients(), recipeToEdit);
        Recipe updatedRecipe = RecipeMapper.toEntity(requestRecipeDTO, recipeToEdit, recipeIngredients);

        return recipeRepository.save(updatedRecipe);
    }

    private Set<RecipeIngredient> getRecipeIngredientsFromDTO(List<RequestRecipeIngredientDTO> recipeIngredientDTOs, Recipe recipe) {
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();

        for(RequestRecipeIngredientDTO dto : recipeIngredientDTOs) {
            RecipeIngredient ingredient = createNew(dto, recipe);
            recipeIngredients.add(ingredient);
        }
        return recipeIngredients;
    }

    private Set<RecipeIngredient> handleUpdateIngredients(List<RequestRecipeIngredientDTO> updatedIngredients, Recipe recipe) {
        Set<RecipeIngredient> savedIngredients = recipe.getRecipeIngredients();

        Map<Integer, RequestRecipeIngredientDTO> updatedMap = updatedIngredients.stream().collect(Collectors.toMap(RequestRecipeIngredientDTO::getIngredientId, Function.identity()));
        Map<Integer, RecipeIngredient> savedMap = savedIngredients.stream().collect(Collectors.toMap(i -> i.getIngredient().getId(), Function.identity()));

        for(Map.Entry<Integer, RequestRecipeIngredientDTO> updated : updatedMap.entrySet()) {
            RecipeIngredient savedValue = savedMap.get(updated.getKey());
            RequestRecipeIngredientDTO updatedValue = updated.getValue();

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
            RequestRecipeIngredientDTO updatedValue = updatedMap.get(saved.getKey());
            if(updatedValue == null) {
                savedIngredients.remove(savedValue);
            }
        }
        return savedIngredients;
    }

    private boolean updatedAndSavedDiffer(RecipeIngredient saved, RequestRecipeIngredientDTO updated) {
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


    private RecipeIngredient createNew(RequestRecipeIngredientDTO dto, Recipe recipe) {
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

}
