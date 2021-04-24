package com.kitchenapp.kitchenappapi.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ShoppingUserIngredient extends AbstractUserIngredient {

    private boolean ticked;

}
