package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.MetricUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    Optional<Measurement> findByNameAndMetricQuantityAndMetricUnit(String name, double metricQuantity, MetricUnit metricUnit);
}
