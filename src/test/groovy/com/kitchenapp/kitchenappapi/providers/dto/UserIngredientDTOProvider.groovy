package com.kitchenapp.kitchenappapi.providers.dto


import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO

import java.time.LocalDate

class UserIngredientDTOProvider {
    static Map DEFAULTS = [
            ingredient: IngredientDTOProvider.make(),
            quantity  : QuantityDTOProvider.make(),
            expiryDate: null,
            dateBought: LocalDate.now()
    ]

    static UserIngredientDTO make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new UserIngredientDTO([
                ingredient: props.ingredient,
                quantity  : props.quantity,
                expiryDate: props.expiryDate,
                dateBought: props.dateBought
        ])
    }
}
