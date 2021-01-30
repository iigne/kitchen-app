package com.kitchenapp.kitchenappapi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserIngredientDTO {
    @NotNull //TODO replace with IngredientDTO
    private int ingredientId;

    private QuantityDTO quantity;

    private QuantityDTO metricQuantity;

    private LocalDate expiryDate;

    private LocalDate dateBought;
}
