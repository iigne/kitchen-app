package com.kitchenapp.kitchenappapi.dto.recipe;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientQuantityDTO {
    private int ingredientId;
    private Integer measurementId;
    private double quantity;
}
