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

    public static List<QuantityDTO> toDTO(UserIngredient userIngredient) {
        List<QuantityDTO> quantities = new ArrayList<>();
        quantities.add(toDTO(userIngredient.getMetricQuantity(), userIngredient.getIngredient().getMetricMeasurement()));
        QuantityDTO custom = userIngredient.getCustomMeasurement() != null ? QuantityMapper.toDTO(userIngredient.getMetricQuantity(), userIngredient.getCustomMeasurement()) : null;
        if(custom != null) {
            quantities.add(custom);
        }
        return quantities;
    }

}
