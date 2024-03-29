package com.kitchenapp.kitchenappapi.providers.model

import com.kitchenapp.kitchenappapi.model.ingredient.Measurement
import com.kitchenapp.kitchenappapi.model.ingredient.MetricUnit
import com.kitchenapp.kitchenappapi.providers.CommonTestData

class MeasurementProvider {

    static Map DEFAULTS = [
            id            : CommonTestData.MEASUREMENT_ID_METRIC,
            name          : "g",
            metricQuantity: 1,
            metricUnit    : MetricUnit.GRAMS
    ]

    static Measurement make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new Measurement([
                id            : props.id,
                name          : props.name,
                metricQuantity: props.metricQuantity,
                metricUnit    : props.metricUnit
        ])
    }
}
