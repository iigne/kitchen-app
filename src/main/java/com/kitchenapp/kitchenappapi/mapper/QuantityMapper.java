package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.dto.QuantityDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.MetricUnit;

public class QuantityMapper {

    public static QuantityDTO toDTO(double metricQuantity, MetricUnit metricUnit) {
        return QuantityDTO.builder()
//                .measurementId(0) TODO probably should do something abut this
                .quantity(metricQuantity)
                .measurementName(metricUnit.name())
                .build();
    }

    public static QuantityDTO toDTO(double metricQuantity, Measurement customUnit) {
        return QuantityDTO.builder()
                .measurementId(customUnit.getId())
                .quantity(MeasurementConverter.toMeasurement(metricQuantity, customUnit))
                .measurementName(customUnit.getName())
                .build();
    }

}
