package com.kitchenapp.kitchenappapi.mapper.useringredient;

import com.kitchenapp.kitchenappapi.dto.useringredient.ShoppingListItemDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.mapper.ingredient.IngredientMapper;
import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient;
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import com.kitchenapp.kitchenappapi.model.useringredient.ShoppingUserIngredient;
import com.kitchenapp.kitchenappapi.model.user.User;

import java.util.List;
import java.util.stream.Collectors;


public class ShoppingListMapper {

    public static List<ShoppingListItemDTO> toDTOs(List<ShoppingUserIngredient> entities) {
        return entities.stream().map(ShoppingListMapper::toDTO).collect(Collectors.toList());
    }

    public static ShoppingListItemDTO toDTO(ShoppingUserIngredient entity) {
        return ShoppingListItemDTO.builder()
                .ingredient(IngredientMapper.toDTO(entity.getIngredient()))
                .quantity(QuantityMapper.toDTO(entity.getMetricQuantity(), entity.getMeasurement()))
                .ticked(entity.isTicked())
                .build();
    }

    public static ShoppingUserIngredient toEntity(Ingredient ingredient, User user, Measurement measurement, double quantity) {
        return ShoppingUserIngredient.builder()
                .user(user)
                .ingredient(ingredient)
                .measurement(measurement)
                .metricQuantity(MeasurementConverter.toMetricIfMetric(quantity, measurement))
                .ticked(false)
                .build();
    }
}
