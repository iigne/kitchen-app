package com.kitchenapp.kitchenappapi.model.useringredient;

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

    private LocalDate dateAdded;

    private LocalDate dateLastUsed;

    private LocalDate dateExpiry;

}
