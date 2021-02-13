package com.kitchenapp.kitchenappapi.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseRecipeIngredientDTO {
    private String ingredientName;
    private double quantity;
    private String measurement;
}

