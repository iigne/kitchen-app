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

    public List<UserIngredient> updateQuantities(final int userId, List<IngredientQuantityDTO> ingredientQuantities) {
        Map<Integer, IngredientQuantityDTO> ingredientsById = ingredientQuantities.stream().collect(Collectors.toMap(IngredientQuantityDTO::getIngredientId, Function.identity()));
        List<Integer> measurementIds = ingredientQuantities.stream().map(IngredientQuantityDTO::getMeasurementId).collect(Collectors.toList());
        Map<Integer, Measurement> measurementsById = measurementService.findByIdsIn(measurementIds).stream().collect(Collectors.toMap(Measurement::getId, Function.identity()));

        List<UserIngredient> userIngredients = userIngredientRepository.findAllByUserIdAndIngredientIdIn(userId, new ArrayList<>(ingredientsById.keySet()));
        for(UserIngredient ingredient : userIngredients) {
            final int ingredientId = ingredient.getId().getIngredientId();
            IngredientQuantityDTO ingredientQuantity = ingredientsById.get(ingredientId);
            if(ingredientQuantity != null) {
                final int measurementId = ingredientQuantity.getMeasurementId();
                Measurement measurement = measurementsById.get(measurementId);
                if(measurement != null) {
                    double toRemoveMetricQuantity = MeasurementConverter.toMetricIfMetric(ingredientQuantity.getQuantity(), measurement);
                    double difference = ingredient.getMetricQuantity() - toRemoveMetricQuantity;
                    ingredient.setMetricQuantity(difference > 0 ? difference : 0);
                } else {
                    throw new EntityNotFoundException(String.format("measurementId %s does not exist", measurementId));
                }
            } else {
                throw new EntityNotFoundException(String.format("userId %s does not have ingredientId %s", userId, ingredientId));
            }
        }
        return userIngredientRepository.saveAll(userIngredients);
    }

    public List<UserIngredient> getByIds(final List<Integer> ingredientIds, final int userId) {
        return userIngredientRepository.findAllByUserIdAndIngredientIdIn(userId, ingredientIds);
    }

    public List<UserIngredient> saveAll(List<UserIngredient> ingredients) {
        return userIngredientRepository.saveAll(ingredients);
    }

}
