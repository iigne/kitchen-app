package com.kitchenapp.kitchenappapi.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class IngredientQuantityDTO {
    private int ingredientId;
    private Integer measurementId;
    private double quantity;
}
