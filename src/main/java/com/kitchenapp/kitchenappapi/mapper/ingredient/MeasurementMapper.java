package com.kitchenapp.kitchenappapi.mapper.ingredient;

import com.kitchenapp.kitchenappapi.dto.ingredient.MeasurementDTO;
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import com.kitchenapp.kitchenappapi.model.ingredient.MetricUnit;

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

    public static List<MeasurementDTO> toDTO(Set<Measurement> entities) {
        return entities.stream().map(MeasurementMapper::toDTO).collect(Collectors.toList());
    }

    public static MeasurementDTO toDTO(Measurement entity) {
        return MeasurementDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .metricUnit(entity.getMetricUnit().name())
                .metricQuantity(entity.getMetricQuantity())
                .build();
    }

}
