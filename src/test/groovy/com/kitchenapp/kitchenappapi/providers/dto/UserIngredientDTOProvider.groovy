package com.kitchenapp.kitchenappapi.providers.dto

import com.kitchenapp.kitchenappapi.dto.IngredientDTO
import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO
import com.kitchenapp.kitchenappapi.model.Category
import com.kitchenapp.kitchenappapi.providers.CommonTestData

import java.time.LocalDate

class UserIngredientDTOProvider {
    static Map DEFAULTS = [
            ingredient    : IngredientDTOProvider.make(),
            quantity      : null,
            metricQuantity: null,
            expiryDate    : null,
            dateBought    : LocalDate.now()
    ]

    static UserIngredientDTO make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new UserIngredientDTO([
                ingredient    : props.ingredient,
                quantity      : props.quantity,
                metricQuantity: props.metricQuantity,
                expiryDate    : props.expiryDate,
                dateBought    : props.dateBought
        ])
    }
}
