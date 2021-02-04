package com.kitchenapp.kitchenappapi.providers.dto

import com.kitchenapp.kitchenappapi.dto.IngredientDTO
import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO
import com.kitchenapp.kitchenappapi.model.Category
import com.kitchenapp.kitchenappapi.providers.CommonTestData

import java.time.LocalDate

class UserIngredientDTOProvider {
    static Map DEFAULTS = [
            ingredient    : IngredientDTOProvider.make(),
            quantities    : [],
            expiryDate    : null,
            dateBought    : LocalDate.now()
    ]

    static UserIngredientDTO make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new UserIngredientDTO([
                ingredient    : props.ingredient,
                quantities    : props.quantities,
                expiryDate    : props.expiryDate,
                dateBought    : props.dateBought
        ])
    }
}
