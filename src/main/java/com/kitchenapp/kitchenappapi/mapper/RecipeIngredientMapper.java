package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.dto.response.ResponseRecipeIngredientDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.Recipe;
import com.kitchenapp.kitchenappapi.model.RecipeIngredient;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeIngredientMapper {

    public static RecipeIngredient toEntity(double quantity, Ingredient ingredient, Measurement measurement, Recipe recipe) {
        return RecipeIngredient.builder()
                .recipe(recipe)
                .ingredient(ingredient)
                .customMeasurement(measurement)
                .metricQuantity(measurement.isMetric() ?  quantity: MeasurementConverter.toMetric(quantity, measurement))
                .build();
    }

    public static ResponseRecipeIngredientDTO toDTO(RecipeIngredient recipeIngredient) {
        Measurement measurement = recipeIngredient.getCustomMeasurement();
        return ResponseRecipeIngredientDTO.builder()
                .ingredientName(recipeIngredient.getIngredient().getName())
                .quantity(measurement.isMetric() ? recipeIngredient.getMetricQuantity() :
                        MeasurementConverter.toMeasurement(recipeIngredient.getMetricQuantity(), measurement))
                .measurement(measurement.getName())
                .build();
    }

    public static List<ResponseRecipeIngredientDTO> toDTOs(Set<RecipeIngredient> recipeIngredients) {
        return recipeIngredients.stream().map(RecipeIngredientMapper::toDTO).collect(Collectors.toList());
    }

    public static RecipeIngredient toEntity(RecipeIngredient recipeIngredient, double quantity, Measurement measurement) {
        recipeIngredient.setCustomMeasurement(measurement);
        recipeIngredient.setMetricQuantity(measurement.isMetric() ?  quantity: MeasurementConverter.toMetric(quantity, measurement));
        return recipeIngredient;
    }
}
