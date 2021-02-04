package com.kitchenapp.kitchenappapi.providers.dto

import com.kitchenapp.kitchenappapi.dto.QuantityDTO
import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO
import com.kitchenapp.kitchenappapi.providers.CommonTestData

class QuantityDTOProvider {
    static Map DEFAULTS = [
            measurementId  : CommonTestData.MEASUREMENT_ID_METRIC,
            quantity       : 1,
            measurementName: "g"
    ]

    static QuantityDTO make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new QuantityDTO([
                measurementId  : props.measurementId,
                quantity       : props.quantity,
                measurementName: props.measurementName
        ])
    }
}
