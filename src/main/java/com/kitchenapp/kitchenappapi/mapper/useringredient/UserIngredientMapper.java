package com.kitchenapp.kitchenappapi.mapper.useringredient;

import com.kitchenapp.kitchenappapi.dto.useringredient.ResponseUserIngredientDTO;
import com.kitchenapp.kitchenappapi.dto.useringredient.RequestUserIngredientDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.mapper.ingredient.IngredientMapper;
import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient;
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import com.kitchenapp.kitchenappapi.model.useringredient.ShoppingUserIngredient;
import com.kitchenapp.kitchenappapi.model.useringredient.UserIngredient;
import com.kitchenapp.kitchenappapi.model.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UserIngredientMapper {

    public static UserIngredient toEntity(RequestUserIngredientDTO dto, Ingredient ingredient, User user, Measurement measurement) {
        return UserIngredient.builder()
                .ingredient(ingredient)
                .user(user)
                .measurement(measurement)
                .metricQuantity(MeasurementConverter.toMetricIfMetric(dto.getQuantity(), measurement))
                .dateAdded(dto.getDateBought() != null ? dto.getDateBought() : LocalDate.now())
                .dateExpiry(dto.getExpiryDate() != null ? dto.getExpiryDate() : LocalDate.now().plusDays(ingredient.getShelfLifeDays()))
                .build();
    }

    public static UserIngredient toEntity(ShoppingUserIngredient shoppingUserIngredient) {
        Ingredient ingredient = shoppingUserIngredient.getIngredient();
        return UserIngredient.builder()
                .ingredient(ingredient)
                .user(shoppingUserIngredient.getUser())
                .measurement(shoppingUserIngredient.getMeasurement())
                .metricQuantity(shoppingUserIngredient.getMetricQuantity())
                .dateAdded(LocalDate.now())
                .dateExpiry(LocalDate.now().plusDays(ingredient.getShelfLifeDays()))
                .build();
    }

    public static ResponseUserIngredientDTO toDTO(UserIngredient entity) {
        return ResponseUserIngredientDTO.builder()
                .ingredient(IngredientMapper.toDTO(entity.getIngredient()))
                .quantity(QuantityMapper.toDTO(entity.getMetricQuantity(), entity.getMeasurement()))
                .dateBought(entity.getDateAdded())
                .expiryDate(entity.getDateExpiry())
                .build();
    }

    public static List<ResponseUserIngredientDTO> toDTO(List<UserIngredient> entities) {
        return entities.stream().map(UserIngredientMapper::toDTO).collect(Collectors.toList());
    }
}
