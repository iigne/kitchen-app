package com.kitchenapp.kitchenappapi.model;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class UserIngredientId implements Serializable {
    private int userId;
    private int ingredientId;
}
