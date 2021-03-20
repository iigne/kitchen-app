package com.kitchenapp.kitchenappapi.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingUserIngredient {

    @EmbeddedId
    private final UserIngredientId id = new UserIngredientId();

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="measurement_id")
    private Measurement measurement;

    private double metricQuantity;

    private boolean ticked;

}
