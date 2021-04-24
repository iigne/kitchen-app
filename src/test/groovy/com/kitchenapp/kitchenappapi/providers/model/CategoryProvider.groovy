package com.kitchenapp.kitchenappapi.providers.model

import com.kitchenapp.kitchenappapi.model.ingredient.Category
import com.kitchenapp.kitchenappapi.providers.CommonTestData

class CategoryProvider {
    static Map DEFAULTS = [
            id: CommonTestData.CATEGORY_ID,
            name: "Fridge",
            shelfLifeDays: 7
    ]

    static Category make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new Category([
                id: props.id,
                name: props.name,
                shelfLifeDays: props.shelfLifeDays
        ])
    }
}
