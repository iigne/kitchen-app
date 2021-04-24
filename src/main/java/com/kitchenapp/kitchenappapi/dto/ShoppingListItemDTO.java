package com.kitchenapp.kitchenappapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ShoppingListItemDTO extends AbstractUserIngredientDTO {

    private boolean ticked;

}
