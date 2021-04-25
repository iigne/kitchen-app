package com.kitchenapp.kitchenappapi.mapper.useringredient;

import com.kitchenapp.kitchenappapi.dto.useringredient.ResponseQuantityDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;

public class QuantityMapper {

    public static ResponseQuantityDTO toDTO(double metricQuantity, Measurement measurement) {
        return ResponseQuantityDTO.builder()
                .measurementId(measurement.getId())
                .quantity(measurement.isMetric() ? metricQuantity : MeasurementConverter.toMeasurement(metricQuantity, measurement) )
                .measurementName(measurement.getName())
                .build();
    }

}
