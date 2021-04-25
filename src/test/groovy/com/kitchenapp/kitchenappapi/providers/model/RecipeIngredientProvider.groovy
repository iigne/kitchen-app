package com.kitchenapp.kitchenappapi.providers.model


import com.kitchenapp.kitchenappapi.model.recipe.RecipeIngredient

class RecipeIngredientProvider {
    static Map DEFAULTS = [
            recipe           : null,
            ingredient       : null,
            customMeasurement: MeasurementProvider.make(),
            metricQuantity   : 1
    ]

    static RecipeIngredient make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new RecipeIngredient([
                recipe           : props.recipe,
                ingredient       : props.ingredient,
                customMeasurement: props.customMeasurement,
                metricQuantity   : props.metricQuantity
        ])
    }
}
