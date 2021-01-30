package com.kitchenapp.kitchenappapi.providers.model

import com.kitchenapp.kitchenappapi.model.Measurement
import com.kitchenapp.kitchenappapi.model.MetricUnit
import com.kitchenapp.kitchenappapi.providers.CommonTestData

class MeasurementProvider {

    static Map DEFAULTS = [
            id            : CommonTestData.MEASUREMENT_ID,
            name          : "Jar",
            metricQuantity: 150,
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
