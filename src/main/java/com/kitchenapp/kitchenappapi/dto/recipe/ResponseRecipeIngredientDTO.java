package com.kitchenapp.kitchenappapi.dto.recipe;

import com.kitchenapp.kitchenappapi.dto.ingredient.MeasurementDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseRecipeIngredientDTO {
    private int ingredientId;
    private String ingredientName;
    private double recipeQuantity;
    private double ownedQuantity;
    private int measurementId;
    private String measurement;
    private List<MeasurementDTO> measurements;
}