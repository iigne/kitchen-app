package com.kitchenapp.kitchenappapi.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserIngredientDTO {
    @NotNull
    private IngredientDTO ingredient;

    private List<QuantityDTO> quantities;

    private LocalDate expiryDate;

    private LocalDate dateBought;

    @JsonIgnore
    public QuantityDTO getQuantity() {
        return quantities.get(0);
    }
}
