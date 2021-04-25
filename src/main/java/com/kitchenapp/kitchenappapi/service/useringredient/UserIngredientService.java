package com.kitchenapp.kitchenappapi.service.useringredient;

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.dto.useringredient.RequestUserIngredientDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.mapper.useringredient.UserIngredientMapper;
import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient;
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import com.kitchenapp.kitchenappapi.model.user.User;
import com.kitchenapp.kitchenappapi.model.useringredient.UserIngredient;
import com.kitchenapp.kitchenappapi.repository.useringredient.UserIngredientRepository;
import com.kitchenapp.kitchenappapi.service.ingredient.IngredientService;
import com.kitchenapp.kitchenappapi.service.ingredient.MeasurementService;
import com.kitchenapp.kitchenappapi.service.user.UserService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserIngredientService extends AbstractUserIngredientService<UserIngredient, UserIngredientRepository, RequestUserIngredientDTO> {

    private final UserIngredientRepository userIngredientRepository;

    @Override
    protected UserIngredientRepository getRepository() {
        return userIngredientRepository;
    }

    @Override
    protected UserIngredient mapToEntity(RequestUserIngredientDTO dto, Ingredient ingredient, User user, Measurement measurement) {
        return UserIngredientMapper.toEntity(dto, ingredient, user, measurement);
    }

    public UserIngredientService(UserIngredientRepository userIngredientRepository, MeasurementService measurementService, UserService userService, IngredientService ingredientService) {
        super(measurementService, userService, ingredientService);
        this.userIngredientRepository = userIngredientRepository;
    }

    /**
     * Used by make recipe functionality, removes given ingredient quantities from user stock.
     *
     * If given ingredient does not exist in user stock, it's ignored.
     * If given ingredient exits, given quantity is removed.
     *
     * @param userId
     * @param ingredientQuantities ingredients and quantities to remove
     * @return list of updated user ingredients
     */
    public List<UserIngredient> updateQuantities(final int userId, List<IngredientQuantityDTO> ingredientQuantities) {
        Map<Integer, IngredientQuantityDTO> ingredientsById = ingredientQuantities.stream().collect(Collectors.toMap(IngredientQuantityDTO::getIngredientId, Function.identity()));
        Map<Integer, Measurement> measurementsById = measurementService.extractMeasurementsFromDTOs(ingredientQuantities);

        List<UserIngredient> userIngredients = userIngredientRepository.findAllByUserIdAndIngredientIdIn(userId, new ArrayList<>(ingredientsById.keySet()));
        for(UserIngredient ingredient : userIngredients) {
            final int ingredientId = ingredient.getId().getIngredientId();
            IngredientQuantityDTO ingredientQuantity = getOrThrow(userId, ingredientId, ingredientsById);

            final int measurementId = ingredientQuantity.getMeasurementId();
            Measurement measurement = measurementService.getFromMapOrThrow(measurementId, measurementsById);

            ingredient.setMetricQuantity(getQuantityDifference(ingredient.getMetricQuantity(), ingredientQuantity.getQuantity(), measurement));
        }
        return userIngredientRepository.saveAll(userIngredients);
    }

    /**
     * Gets ingredient's new quantity (in metric) after removing a quantity.
     * If after removing quantity it reaches negative numbers, 0 is returned.
     *
     * @param currentMetricQuantity user ingredient quantity
     * @param removedQuantity
     * @param measurement of removedQuantity
     * @return new metric quantity
     */
    private double getQuantityDifference(final double currentMetricQuantity, final double removedQuantity, Measurement measurement) {
        double toRemoveMetricQuantity = MeasurementConverter.toMetricIfMetric(removedQuantity, measurement);
        double difference = currentMetricQuantity - toRemoveMetricQuantity;
        return difference > 0 ? difference : 0;
    }

    private IngredientQuantityDTO getOrThrow(final int userId, final int ingredientId, final Map<Integer, IngredientQuantityDTO> ingredientsById) {
        IngredientQuantityDTO ingredientQuantity = ingredientsById.get(ingredientId);
        if(ingredientQuantity == null) {
            throw new EntityNotFoundException(String.format("userId %s does not have ingredientId %s", userId, ingredientId));
        }
        return ingredientQuantity;
    }

    public List<UserIngredient> getByIds(final List<Integer> ingredientIds, final int userId) {
        return userIngredientRepository.findAllByUserIdAndIngredientIdIn(userId, ingredientIds);
    }

    public List<UserIngredient> saveAll(List<UserIngredient> ingredients) {
        return userIngredientRepository.saveAll(ingredients);
    }

}
