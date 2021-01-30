package com.kitchenapp.kitchenappapi.providers.model


import com.kitchenapp.kitchenappapi.model.UserIngredient

import java.time.LocalDate

class UserIngredientProvider {

    static Map DEFAULTS = [
            ingredient       : IngredientProvider.make(),
            customMeasurement: MeasurementProvider.make(),
            user             : null,
            metricQuantity   : 100,
            dateAdded        : LocalDate.now(),
            dateExpiry       : null
    ]

    static UserIngredient make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new UserIngredient([
                ingredient       : props.ingredient,
                customMeasurement: props.customMeasurement,
                user             : props.user,
                metricQuantity   : props.metricQuantity,
                dateAdded        : props.dateAdded,
                dateExpiry       : props.dateExpiry
        ])
    }

}
