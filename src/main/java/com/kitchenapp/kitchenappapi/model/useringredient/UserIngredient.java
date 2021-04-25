package com.kitchenapp.kitchenappapi.model.useringredient;

import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UserIngredient extends AbstractUserIngredient {

    @ManyToOne
    @JoinColumn(name="custom_measurement_id")
    private Measurement customMeasurement;

    private LocalDate dateAdded;

    private LocalDate dateLastUsed;

    private LocalDate dateExpiry;

}
