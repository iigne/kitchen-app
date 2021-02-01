package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.QuantityDTO;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.MetricUnit;
import com.kitchenapp.kitchenappapi.repository.IngredientRepository;
import com.kitchenapp.kitchenappapi.repository.MeasurementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    public Measurement findByIdOrThrow(final int id) {
        return measurementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("measurementId %s not found", id)));
    }

    public Measurement getMetricMeasurement(MetricUnit metricUnit) {
        return measurementRepository.findByNameAndMetricQuantity(metricUnit.getAbbreviation(), 1.0)
                .orElseThrow(() -> new EntityNotFoundException(String.format("metric measurement %s not found in the database", metricUnit.name())));
    }

    public Optional<Measurement> findByNameQuantityUnit(String name, double quantity, MetricUnit unit) {
        return measurementRepository.findByNameAndMetricQuantityAndMetricUnit(name, quantity, unit);
    }


}
