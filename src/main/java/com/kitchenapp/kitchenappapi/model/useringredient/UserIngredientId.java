package com.kitchenapp.kitchenappapi.model.useringredient;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@Getter
public class UserIngredientId implements Serializable {
    private int userId;
    private int ingredientId;
}
