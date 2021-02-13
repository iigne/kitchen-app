package com.kitchenapp.kitchenappapi.model;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeIngredient {
    @EmbeddedId
//    @EqualsAndHashCode.Include
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
//    @EqualsAndHashCode.Include
    private Measurement customMeasurement;

//    @EqualsAndHashCode.Include
    private double metricQuantity;

}
