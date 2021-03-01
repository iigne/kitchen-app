package com.kitchenapp.kitchenappapi.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseRecipeIngredientDTO {
    private int ingredientId;
    private String ingredientName;
    private double recipeQuantity;
    private double ownedQuantity;
    private int measurementId;
    private String measurement;
}