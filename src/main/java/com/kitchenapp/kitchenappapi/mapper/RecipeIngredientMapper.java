package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.Recipe;
import com.kitchenapp.kitchenappapi.model.RecipeIngredient;

public class RecipeIngredientMapper {

    public static RecipeIngredient toEntity(double quantity, Ingredient ingredient, Measurement measurement, Recipe recipe) {
        return RecipeIngredient.builder()
                .recipe(recipe)
                .ingredient(ingredient)
                .customMeasurement(measurement)
                .metricQuantity(measurement.isMetric() ?  quantity: MeasurementConverter.toMetric(quantity, measurement))
                .build();
    }
}
