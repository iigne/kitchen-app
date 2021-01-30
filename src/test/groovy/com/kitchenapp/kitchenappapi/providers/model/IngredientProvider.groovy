package com.kitchenapp.kitchenappapi.providers.model

import com.kitchenapp.kitchenappapi.model.Ingredient
import com.kitchenapp.kitchenappapi.model.MetricUnit
import com.kitchenapp.kitchenappapi.providers.CommonTestData

class IngredientProvider {

    static Map DEFAULTS = [
            id: CommonTestData.INGREDIENT_ID,
            name: "Pesto",
            metricUnit: MetricUnit.GRAMS,
            category: CategoryProvider.make(),
            shelfLifeDays: 14,
            measurements: []
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
