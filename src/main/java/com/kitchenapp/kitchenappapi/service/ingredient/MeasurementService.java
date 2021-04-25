package com.kitchenapp.kitchenappapi.service.ingredient;

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import com.kitchenapp.kitchenappapi.model.ingredient.MetricUnit;
import com.kitchenapp.kitchenappapi.repository.ingredient.MeasurementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    public Measurement findByIdOrThrow(final int id) {
        return measurementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("measurementId %s not found", id)));
    }

    public Measurement getMetricMeasurementOrThrow(MetricUnit metricUnit) {
        return measurementRepository.findByNameAndMetricQuantity(metricUnit.getAbbreviation(), 1.0)
                .orElseThrow(() -> new EntityNotFoundException(String.format("metric measurement %s not found in the database", metricUnit.name())));
    }

    public Optional<Measurement> findByNameQuantityUnit(String name, double quantity, MetricUnit unit) {
        return measurementRepository.findByNameAndMetricQuantityAndMetricUnit(name, quantity, unit);
    }

    public Map<Integer, Measurement> extractMeasurementsFromDTOs(List<IngredientQuantityDTO> ingredientQuantityDTOs) {
        List<Integer> measurementIds = ingredientQuantityDTOs.stream().map(IngredientQuantityDTO::getMeasurementId).collect(Collectors.toList());
        List<Measurement> measurements = measurementRepository.findAllByIdIn(measurementIds);
        return measurements.stream().collect(Collectors.toMap(Measurement::getId, Function.identity()));
    }

    public Measurement getFromMapOrThrow(final int measurementId, final Map<Integer, Measurement> measurementsById) {
        Measurement measurement = measurementsById.get(measurementId);
        if (measurement == null) {
            throw new EntityNotFoundException(String.format("measurementId %s does not exist", measurementId));
        }
        return measurement;
    }
}
