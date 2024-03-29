package com.kitchenapp.kitchenappapi.service.useringredient;

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient;
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import com.kitchenapp.kitchenappapi.model.useringredient.AbstractUserIngredient;
import com.kitchenapp.kitchenappapi.model.useringredient.UserIngredientId;
import com.kitchenapp.kitchenappapi.model.user.User;
import com.kitchenapp.kitchenappapi.repository.useringredient.AbstractUserIngredientRepository;
import com.kitchenapp.kitchenappapi.service.ingredient.IngredientService;
import com.kitchenapp.kitchenappapi.service.ingredient.MeasurementService;
import com.kitchenapp.kitchenappapi.service.user.UserService;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * An abstract class providing common functionality for both UserIngredient and ShoppingUserIngredient.
 *
 * @param <T> type of entity (UserIngredient/ShoppingUserIngredient)
 * @param <R> repository (UserIngredientRepository/ShoppingListRepository)
 * @param <D> type of DTO (IngredientQuantityDTO for ShoppingListIngredient, RequestUserIngredientDTO for UserIngredient)
 */
@RequiredArgsConstructor
public abstract class AbstractUserIngredientService<T extends AbstractUserIngredient, R extends AbstractUserIngredientRepository<T, UserIngredientId>, D extends IngredientQuantityDTO> {

    protected final MeasurementService measurementService;
    protected final UserService userService;
    protected final IngredientService ingredientService;

    /**
     * Gets the implementation-specific repository
     *
     * @return UserIngredientRepository or ShoppingListRepository
     */
    protected abstract R getRepository();

    /**
     * Maps to the implementation-specific entity
     *
     * @return UserIngredient or ShoppingUserIngredient
     */
    protected abstract T mapToEntity(final D dto, Ingredient ingredient, User user, Measurement measurement);

    public List<T> findAllByUserId(final int userId) {
        return getRepository().findAllByUserId(userId);
    }

    public T getByIngredientIdAndUserIdOrThrow(final int ingredientId, final int userId) {
        return getRepository().findByUserIdAndIngredientId(userId, ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("userId %s and ingredientId %s doesn't exist", userId, ingredientId)));
    }

    public void deleteByIngredientAndUserId(final int ingredientId, final  int userId) {
        T ingredient = getByIngredientIdAndUserIdOrThrow(ingredientId, userId);
        getRepository().delete(ingredient);
    }

    /**
     * Called from update endpoint, updates existing UserIngredient from DTO
     *
     * @return updated entity
     */
    public T update(final int userId, final D dto) {
        final int ingredientId = dto.getIngredientId();
        final int measurementId = dto.getMeasurementId();
        final double quantity = dto.getQuantity();

        T userIngredient = getByIngredientIdAndUserIdOrThrow(ingredientId, userId);
        Measurement measurement = measurementService.findByIdOrThrow(measurementId);

        return updateGivenItem(userIngredient, measurement, quantity);
    }

    /**
     * Called from create endpoint, adds quantity from DTO to existing entity
     *
     * @return updated entity
     */
    public T update(final T userIngredient, final double addedQuantity, Measurement addedMeasurement) {
        double addedQuantityInMetric = MeasurementConverter.toMetricIfMetric(addedQuantity, addedMeasurement);
        double totalMetricQuantity = userIngredient.getMetricQuantity() + addedQuantityInMetric;
        double newQuantity = MeasurementConverter.toMeasurement(totalMetricQuantity, userIngredient.getMeasurement());

        //keeping original measurement if item has been added this way
        return updateGivenItem(userIngredient, userIngredient.getMeasurement(), newQuantity);
    }

    public T updateGivenItem(T userIngredient, Measurement newMeasurement, double newQuantity) {
        double metricQuantity = MeasurementConverter.toMetricIfMetric(newQuantity, newMeasurement);
        userIngredient.setMetricQuantity(metricQuantity);
        userIngredient.setMeasurement(newMeasurement);
        return getRepository().save(userIngredient);
    }

    /**
     * Creates (if entity does not exist) or updates user ingredient from DTO
     *
     * @return created/updated entity
     */
    public T create(final int userId, final D dto) {
        final int ingredientId = dto.getIngredientId();
        final int measurementId = dto.getMeasurementId();
        final double quantity = dto.getQuantity();

        User user = userService.findByIdOrThrow(userId);
        Ingredient ingredient = ingredientService.findByIdOrThrow(ingredientId);
        Measurement measurement = measurementService.findByIdOrThrow(measurementId);
        Optional<T> existingItem = getRepository().findByUserIdAndIngredientId(userId, ingredientId);
        if(existingItem.isPresent()) {
            return update(existingItem.get(), quantity, measurement);
        }
        return getRepository().save(mapToEntity(dto, ingredient, user, measurement));
    }
}
