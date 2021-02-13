package com.kitchenapp.kitchenappapi.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestRecipeIngredientDTO {
    private int ingredientId;
    private Integer measurementId;
    private double quantity;
}
