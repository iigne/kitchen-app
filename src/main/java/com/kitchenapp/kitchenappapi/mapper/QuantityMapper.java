package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.dto.QuantityDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.MetricUnit;
import com.kitchenapp.kitchenappapi.model.UserIngredient;

import java.util.ArrayList;
import java.util.List;

public class QuantityMapper {

    public static QuantityDTO toDTO(double metricQuantity, Measurement measurement) {
        return QuantityDTO.builder()
                .measurementId(measurement.getId())
                .quantity(measurement.isMetric() ? metricQuantity : MeasurementConverter.toMeasurement(metricQuantity, measurement) )
                .measurementName(measurement.getName())
                .build();
    }

}
