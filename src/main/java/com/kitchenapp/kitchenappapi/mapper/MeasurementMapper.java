package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.dto.MeasurementDTO;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.MetricUnit;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MeasurementMapper {

    public static Set<Measurement> toEntity(List<MeasurementDTO> dtos) {
        return dtos.stream().map(MeasurementMapper::toEntity).collect(Collectors.toSet());
    }

    public static Measurement toEntity(MeasurementDTO dto) {
        return Measurement.builder()
                .name(dto.getName())
                .metricQuantity(dto.getMetricQuantity())
                .metricUnit(MetricUnit.get(dto.getMetricUnit()))
                .build();
    }
}
