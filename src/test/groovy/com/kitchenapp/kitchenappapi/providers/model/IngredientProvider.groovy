package com.kitchenapp.kitchenappapi.providers.model

import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient
import com.kitchenapp.kitchenappapi.model.ingredient.MetricUnit
import com.kitchenapp.kitchenappapi.providers.CommonTestData

class IngredientProvider {

    static Map DEFAULTS = [
            id: CommonTestData.INGREDIENT_ID,
            name: "Pesto",
            metricUnit: MetricUnit.GRAMS,
            category: CategoryProvider.make(),
            shelfLifeDays: 14,
            measurements: [MeasurementProvider.make()]
    ]

    static Ingredient make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new Ingredient([
                id: props.id,
                name: props.name,
                metricUnit: props.metricUnit,
                category: props.category,
                shelfLifeDays: props.shelfLifeDays,
                measurements: props.measurements
        ])
    }
}
