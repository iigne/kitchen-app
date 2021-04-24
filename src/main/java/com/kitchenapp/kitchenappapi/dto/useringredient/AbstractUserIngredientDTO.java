package com.kitchenapp.kitchenappapi.dto.useringredient;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public abstract class AbstractUserIngredientDTO {
    @NotNull
    @JsonUnwrapped
    protected IngredientDTO ingredient;

    @JsonUnwrapped
    protected ResponseQuantityDTO quantity;
}
