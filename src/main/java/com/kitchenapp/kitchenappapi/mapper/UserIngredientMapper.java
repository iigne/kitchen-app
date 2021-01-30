package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.User;
import com.kitchenapp.kitchenappapi.model.UserIngredient;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UserIngredientMapper {

    public static UserIngredient toEntity(UserIngredientDTO dto, Ingredient ingredient, User user, Measurement customMeasurement) {
        return UserIngredient.builder()
                .ingredient(ingredient)
                .user(user)
                .customMeasurement(customMeasurement)
                .metricQuantity(dto.getMetricQuantity() != null ? dto.getMetricQuantity().getQuantity() :
                        MeasurementConverter.toMetric(dto.getQuantity().getQuantity(), customMeasurement))
                .dateAdded(dto.getDateBought() != null ? dto.getDateBought() : LocalDate.now())
                .dateExpiry(dto.getExpiryDate() != null ? dto.getExpiryDate() : LocalDate.now().plusDays(ingredient.getShelfLifeDays()))
                .build();
    }

    public static UserIngredientDTO toDTO(UserIngredient entity) {
        return UserIngredientDTO.builder()
                .ingredient(IngredientMapper.toDTO(entity.getIngredient()))
                .metricQuantity(QuantityMapper.toDTO(entity.getMetricQuantity(), entity.getIngredient().getMetricUnit()))
                .quantity(entity.getCustomMeasurement() != null ? QuantityMapper.toDTO(entity.getMetricQuantity(), entity.getCustomMeasurement()) : null)
                .dateBought(entity.getDateAdded())
                .expiryDate(entity.getDateExpiry())
                .build();
    }

    public static List<UserIngredientDTO> toDTO(List<UserIngredient> entities) {
        return entities.stream().map(UserIngredientMapper::toDTO).collect(Collectors.toList());
    }
}
