package com.kitchenapp.kitchenappapi.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserIngredient {
    @EmbeddedId
    private final UserIngredientId id = new UserIngredientId();

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

    private double metricQuantity;

    private LocalDate dateAdded;

    private LocalDate dateLastUsed;

    private LocalDate dateExpiry;

}
