package com.kitchenapp.kitchenappapi.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShoppingListItemDTO {
    @NotNull
    @JsonUnwrapped
    private IngredientDTO ingredient;

    @JsonUnwrapped
    private QuantityDTO quantity;

    private boolean ticked;
}
