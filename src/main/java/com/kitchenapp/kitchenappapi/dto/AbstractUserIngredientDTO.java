package com.kitchenapp.kitchenappapi.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
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
    protected QuantityDTO quantity;
}
