package com.kitchenapp.kitchenappapi.providers.model

import com.kitchenapp.kitchenappapi.model.useringredient.ShoppingUserIngredient

class ShoppingUserIngredientProvider {

    static Map DEFAULTS = [
            ingredient    : IngredientProvider.make(),
            user          : UserProvider.make(),
            measurement   : MeasurementProvider.make(),
            metricQuantity: 100,
            ticked        : false
    ]

    static ShoppingUserIngredient make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new ShoppingUserIngredient([
                ingredient    : props.ingredient,
                user          : props.user,
                measurement   : props.measurement,
                metricQuantity: props.metricQuantity,
                ticked        : props.ticked
        ])
    }

}
