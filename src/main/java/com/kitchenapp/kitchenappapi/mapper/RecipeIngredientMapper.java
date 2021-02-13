package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.dto.response.ResponseRecipeIngredientDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.Recipe;
import com.kitchenapp.kitchenappapi.model.RecipeIngredient;
import com.kitchenapp.kitchenappapi.repository.projection.RecipeUserIngredient;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RecipeIngredientMapper {

    public static RecipeIngredient toEntity(double quantity, Ingredient ingredient, Measurement measurement, Recipe recipe) {
        return RecipeIngredient.builder()
                .recipe(recipe)
                .ingredient(ingredient)
                .customMeasurement(measurement)
                .metricQuantity(measurement.isMetric() ? quantity : MeasurementConverter.toMetric(quantity, measurement))
                .build();
    }

//    public static ResponseRecipeIngredientDTO toDTO(RecipeIngredient recipeIngredient) {
//        Measurement measurement = recipeIngredient.getCustomMeasurement();
//        return ResponseRecipeIngredientDTO.builder()
//                .ingredientName(recipeIngredient.getIngredient().getName())
//                .recipeQuantity(measurement.isMetric() ? recipeIngredient.getMetricQuantity() :
//                        MeasurementConverter.toMeasurement(recipeIngredient.getMetricQuantity(), measurement))
//                .measurement(measurement.getName())
//                .build();
//    }
//
//    public static List<ResponseRecipeIngredientDTO> toDTOs(Set<RecipeIngredient> recipeIngredients) {
//        return recipeIngredients.stream().map(RecipeIngredientMapper::toDTO).collect(Collectors.toList());
//    }

    public static List<ResponseRecipeIngredientDTO> toDTOs(List<RecipeUserIngredient> recipeUserIngredients, Set<RecipeIngredient> recipeIngredients) {
        Map<Integer, RecipeUserIngredient> ingredientMap = recipeUserIngredients.stream()
                .collect(Collectors.toMap(RecipeUserIngredient::getIngredientId, Function.identity()));

        return recipeIngredients.stream().map(r -> toDTO(ingredientMap.get(r.getId().getIngredientId()), r)).collect(Collectors.toList());
    }

    public static ResponseRecipeIngredientDTO toDTO(RecipeUserIngredient recipeUserIngredient, RecipeIngredient recipeIngredient) {
        Measurement measurement = recipeIngredient.getCustomMeasurement();
        double userQuantity = recipeUserIngredient.getUserQuantityMetric() != null ? recipeUserIngredient.getUserQuantityMetric() : 0;
        double recipeQuantity = recipeUserIngredient.getRecipeQuantityMetric();
        return ResponseRecipeIngredientDTO.builder()
                .ingredientName(recipeUserIngredient.getIngredientName())
                .measurement(measurement.getName())
                .ownedQuantity(measurement.isMetric() ? userQuantity : MeasurementConverter.toMeasurement(userQuantity, measurement))
                .recipeQuantity(measurement.isMetric() ? recipeQuantity : MeasurementConverter.toMeasurement(recipeQuantity, measurement))
                .build();
    }

    public static RecipeIngredient toEntity(RecipeIngredient recipeIngredient, double quantity, Measurement measurement) {
        recipeIngredient.setCustomMeasurement(measurement);
        recipeIngredient.setMetricQuantity(measurement.isMetric() ? quantity : MeasurementConverter.toMetric(quantity, measurement));
        return recipeIngredient;
    }
}
