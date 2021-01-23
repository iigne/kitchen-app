package com.kitchenapp.kitchenappapi.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class UserIngredient {
    @EmbeddedId
    private UserIngredientId id;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name="custom_measurement_id")
    private Measurement customMeasurement;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    private int metricQuantity;

    private LocalDate dateAdded;

    private LocalDate dateLastUsed;

    private LocalDate dateExpiry;

}
