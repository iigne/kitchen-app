package com.kitchenapp.kitchenappapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientDTO {
    private int ingredientId;
    private Integer measurementId;
    private double quantity;
}
