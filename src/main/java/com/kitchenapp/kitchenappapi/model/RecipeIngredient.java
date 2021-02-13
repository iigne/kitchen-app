package com.kitchenapp.kitchenappapi.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredient {
    @EmbeddedId
    private final RecipeIngredientId id = new RecipeIngredientId();

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name="custom_measurement_id")
    private Measurement customMeasurement;

    private double metricQuantity;

}
