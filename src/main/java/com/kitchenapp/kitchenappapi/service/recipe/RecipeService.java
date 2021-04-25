package com.kitchenapp.kitchenappapi.service.recipe;

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.dto.recipe.RequestRecipeDTO;
import com.kitchenapp.kitchenappapi.dto.recipe.ResponseRecipeDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.mapper.recipe.RecipeIngredientMapper;
import com.kitchenapp.kitchenappapi.mapper.recipe.RecipeMapper;
import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient;
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import com.kitchenapp.kitchenappapi.model.recipe.Recipe;
import com.kitchenapp.kitchenappapi.model.recipe.RecipeIngredient;
import com.kitchenapp.kitchenappapi.model.user.User;
import com.kitchenapp.kitchenappapi.repository.recipe.RecipeIngredientRepository;
import com.kitchenapp.kitchenappapi.repository.recipe.RecipeRepository;
import com.kitchenapp.kitchenappapi.repository.recipe.RecipeUserIngredient;
import com.kitchenapp.kitchenappapi.service.ingredient.IngredientService;
import com.kitchenapp.kitchenappapi.service.ingredient.MeasurementService;
import com.kitchenapp.kitchenappapi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Creates new recipe.
     * Using <code>@Transactional</code> due to there being multiple save operations
     * (first recipe is saved, and then ingredients are added to the recipe and saved,
     * because data conflicts happen otherwise)
     *
     * @param requestRecipeDTO
     * @param userId
     * @return saved recipe converted to DTO
     */
    @Transactional
    public ResponseRecipeDTO create(RequestRecipeDTO requestRecipeDTO, final int userId) {

        User user = userService.findByIdOrThrow(userId);
        Recipe recipe = recipeRepository.save(RecipeMapper.toEntity(requestRecipeDTO, user));

        Set<RecipeIngredient> recipeIngredients = createRecipeIngredientsFromDTO(requestRecipeDTO.getIngredients(), recipe);

        recipe.setRecipeIngredients(recipeIngredients);
        Recipe savedRecipe = recipeRepository.save(recipe);

        List<RecipeUserIngredient> recipeUserIngredients = recipeIngredientRepository.fetchIngredientQuantitiesByUserIdAndRecipeId(userId, savedRecipe.getId());
        return RecipeMapper.toDTO(savedRecipe, recipeUserIngredients, userId);
    }

    /**
     * Updates given recipe details.
     * <p>
     * If recipe ingredients have been changed, perform additional checks to determine whether they have been
     * added, removed or updated.
     *
     * @param requestRecipeDTO
     * @param userId
     * @return DTO containing saved updated recipe
     */
    public ResponseRecipeDTO update(RequestRecipeDTO requestRecipeDTO, final int userId) {
        Recipe recipeToEdit = getByIdOrThrow(requestRecipeDTO.getId());
        verifyRecipeAuthorIsCurrentUser(userId, recipeToEdit.getAuthor().getId());

        Set<RecipeIngredient> recipeIngredients = handleUpdateIngredients(requestRecipeDTO.getIngredients(), recipeToEdit);
        Recipe updatedRecipe = RecipeMapper.toEntity(requestRecipeDTO, recipeToEdit, recipeIngredients);
        Recipe savedRecipe = recipeRepository.save(updatedRecipe);
        List<RecipeUserIngredient> recipeUserIngredients = recipeIngredientRepository.fetchIngredientQuantitiesByUserIdAndRecipeId(userId, savedRecipe.getId());
        return RecipeMapper.toDTO(savedRecipe, recipeUserIngredients, userId);
    }

    public void delete(final int recipeId, final int userId) {
        Recipe recipe = getByIdOrThrow(recipeId);
        verifyRecipeAuthorIsCurrentUser(userId, recipe.getAuthor().getId());
        recipeRepository.delete(recipe);
    }

    /**
     * Toggle handling liking/unliking of recipes.
     * If like - add user to recipe users
     * If unlike - remove user from recipe users
     *
     * @param recipeId
     * @param userId
     * @return whether user is in recipe users after update
     */
    public boolean removeOrAddFromUserRecipes(final int recipeId, final int userId) {
        User user = userService.findByIdOrThrow(userId);
        Recipe recipe = getByIdOrThrow(recipeId);

        boolean isRecipeLiked = recipe.getUsers().stream().anyMatch(u -> u.getId() == userId);
        if (isRecipeLiked) {
            recipe.getUsers().removeIf(u -> u.getId() == userId);
        } else {
            recipe.getUsers().add(user);
        }

        return recipeRepository.save(recipe).getUsers().stream().anyMatch(u -> u.getId() == userId);
    }

    /**
     * Creates entity objects for RecipeIngredient from DTOs.
     *
     * @param recipeIngredientDTOs
     * @param recipe
     * @return set of created entity objects
     */
    private Set<RecipeIngredient> createRecipeIngredientsFromDTO(List<IngredientQuantityDTO> recipeIngredientDTOs, Recipe recipe) {
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();

        //fetching measurements and ingredients now so that no database access is done during the loop which is a performance killer
        Map<Integer, Ingredient> ingredientsById = ingredientService.extractIngredientsFromDTOs(recipeIngredientDTOs);
        Map<Integer, Measurement> measurementsById = measurementService.extractMeasurementsFromDTOs(recipeIngredientDTOs);

        for (IngredientQuantityDTO dto : recipeIngredientDTOs) {
            RecipeIngredient ingredient = createNew(dto, recipe, ingredientsById, measurementsById);
            recipeIngredients.add(ingredient);
        }
        return recipeIngredients;
    }


    /**
     * Handles recipe ingredient update.
     * If ingredient in DTO list, but not in recipe ingredient list - create and add recipe ingredient to list.
     * If ingredient not in DTO list, but in recipe ingredient list - remove from recipe ingredient list.
     * If ingredient in both lists:
     * - if ingredient measurement/quantity same - do nothing
     * - if ingredient measurement/quantity differs - remove from list, update object with new measurement and quantity, add to list.
     *
     * @param updatedIngredients
     * @param recipe
     * @return
     */
    private Set<RecipeIngredient> handleUpdateIngredients(List<IngredientQuantityDTO> updatedIngredients, Recipe recipe) {
        Set<RecipeIngredient> savedIngredients = recipe.getRecipeIngredients();

        Map<Integer, IngredientQuantityDTO> updatedMap = updatedIngredients.stream().collect(Collectors.toMap(IngredientQuantityDTO::getIngredientId, Function.identity()));
        Map<Integer, RecipeIngredient> savedMap = savedIngredients.stream().collect(Collectors.toMap(i -> i.getIngredient().getId(), Function.identity()));

        //fetching measurements and ingredients now so that no database access is done during the loop which is a performance killer
        Map<Integer, Ingredient> ingredientsById = ingredientService.extractIngredientsFromDTOs(updatedIngredients);
        Map<Integer, Measurement> measurementsById = measurementService.extractMeasurementsFromDTOs(updatedIngredients);

        for (Map.Entry<Integer, IngredientQuantityDTO> updated : updatedMap.entrySet()) {
            RecipeIngredient savedValue = savedMap.get(updated.getKey());
            IngredientQuantityDTO updatedValue = updated.getValue();

            //handle add new ingredient
            if (savedValue == null) {
                savedIngredients.add(createNew(updatedValue, recipe, ingredientsById, measurementsById));
            } else {
                //handle update
                if (updatedAndSavedDiffer(savedValue, updatedValue)) {
                    Measurement newMeasurement = measurementService.getFromMapOrThrow(updatedValue.getMeasurementId(), measurementsById);
                    savedIngredients.remove(savedValue);
                    savedIngredients.add(RecipeIngredientMapper.toEntity(savedValue, updatedValue.getQuantity(), newMeasurement));
                }
            }
        }
        //handle remove
        for (Map.Entry<Integer, RecipeIngredient> saved : savedMap.entrySet()) {
            RecipeIngredient savedValue = saved.getValue();
            IngredientQuantityDTO updatedValue = updatedMap.get(saved.getKey());
            if (updatedValue == null) {
                savedIngredients.remove(savedValue);
            }
        }
        return savedIngredients;
    }

    private RecipeIngredient createNew(IngredientQuantityDTO dto, Recipe recipe,
                                       Map<Integer, Ingredient> ingredientsById, Map<Integer, Measurement> measurementsById) {
        Ingredient ingredient = getFromMapOrThrow(dto.getIngredientId(), ingredientsById);
        Measurement measurement = measurementService.getFromMapOrThrow(dto.getMeasurementId(), measurementsById);
        return RecipeIngredientMapper.toEntity(dto.getQuantity(), ingredient, measurement, recipe);
    }

    public Ingredient getFromMapOrThrow(final int ingredientId, final Map<Integer, Ingredient> ingredientsById) {
        Ingredient ingredient = ingredientsById.get(ingredientId);
        if (ingredient == null) {
            throw new EntityNotFoundException(String.format("ingredientId %s does not exist", ingredientId));
        }
        return ingredient;
    }

    /**
     * Checks whether saved and updated have different measurements
     * or different quantities
     *
     * @param saved
     * @param updated
     * @return whether saved and updated differ
     */
    private boolean updatedAndSavedDiffer(RecipeIngredient saved, IngredientQuantityDTO updated) {
        if (saved.getCustomMeasurement().getId() != updated.getMeasurementId()) {
            return true;
        }
        if (saved.getCustomMeasurement().isMetric()) {
            return !(updated.getQuantity() == saved.getMetricQuantity());
        }
        return !(MeasurementConverter.toMetric(updated.getQuantity(), saved.getCustomMeasurement()) == saved.getMetricQuantity());
    }

    private void verifyRecipeAuthorIsCurrentUser(final int userId, final int recipeAuthorId) {
        if (userId != recipeAuthorId) {
            throw new UnsupportedOperationException(String.format("user %s is not allowed to alter recipe created by user %s", userId, recipeAuthorId));
        }
    }

}
