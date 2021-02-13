package com.kitchenapp.kitchenappapi.model;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class RecipeIngredientId implements Serializable {
    private int recipeId;
    private int ingredientId;
}
