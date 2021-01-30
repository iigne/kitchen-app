package com.kitchenapp.kitchenappapi.providers.dto

import com.kitchenapp.kitchenappapi.dto.IngredientDTO
import com.kitchenapp.kitchenappapi.model.MetricUnit
import com.kitchenapp.kitchenappapi.providers.CommonTestData

class IngredientDTOProvider {

    static Map DEFAULTS = [
            id: CommonTestData.INGREDIENT_ID,
            name: CommonTestData.INGREDIENT_NAME,
            metricUnit: MetricUnit.GRAMS.name(),
            category: CommonTestData.CATEGORY_NAME,
            shelfLifeDays: 14,
            measurements: []
    ]

    static IngredientDTO make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new IngredientDTO([
                id: props.id,
                name: props.name,
                metricUnit: props.metricUnit,
                category: props.category,
                shelfLifeDays: props.shelfLifeDays,
                measurements: props.measurements
        ])
    }

}
