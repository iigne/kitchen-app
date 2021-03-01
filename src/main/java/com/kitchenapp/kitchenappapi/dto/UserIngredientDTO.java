package com.kitchenapp.kitchenappapi.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserIngredientDTO {
    @NotNull
    @JsonUnwrapped
    private IngredientDTO ingredient;

    @JsonUnwrapped
    private QuantityDTO quantity;

    private LocalDate expiryDate;

    private LocalDate dateBought;

}
