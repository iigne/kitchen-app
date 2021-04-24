package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.AbstractUserIngredientDTO;
import com.kitchenapp.kitchenappapi.dto.recipe.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.model.*;
import com.kitchenapp.kitchenappapi.repository.AbstractUserIngredientRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractUserIngredientService<T extends AbstractUserIngredient, R extends AbstractUserIngredientRepository<T, UserIngredientId>, DTO extends IngredientQuantityDTO> {

    protected final MeasurementService measurementService;
    protected final UserService userService;
    protected final IngredientService ingredientService;

    protected abstract R getRepository();

    protected abstract T mapToEntity(final DTO dto, Ingredient ingredient, User user, Measurement measurement);

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

    public T update(final int userId, final DTO dto) {
        final int ingredientId = dto.getIngredientId();
        final int measurementId = dto.getMeasurementId();
        final double quantity = dto.getQuantity();

        T userIngredient = getByIngredientIdAndUserIdOrThrow(ingredientId, userId);
        Measurement measurement = measurementService.findByIdOrThrow(measurementId);

        return updateGivenItem(userIngredient, measurement, quantity);
    }

    public T update(final int userId, final int ingredientId, final int measurementId, final double quantity) {
        T userIngredient = getByIngredientIdAndUserIdOrThrow(ingredientId, userId);
        Measurement measurement = measurementService.findByIdOrThrow(measurementId);

        return updateGivenItem(userIngredient, measurement, quantity);
    }

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

    public T create(final int userId, final DTO dto) {
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
