package com.kitchenapp.kitchenappapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserIngredientDTO extends AbstractUserIngredientDTO {

    private LocalDate expiryDate;

    private LocalDate dateBought;

}
