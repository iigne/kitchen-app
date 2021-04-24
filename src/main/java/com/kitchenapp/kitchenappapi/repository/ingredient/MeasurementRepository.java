package com.kitchenapp.kitchenappapi.repository.ingredient;

import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import com.kitchenapp.kitchenappapi.model.ingredient.MetricUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    Optional<Measurement> findByNameAndMetricQuantityAndMetricUnit(String name, double metricQuantity, MetricUnit metricUnit);

    Optional<Measurement> findByNameAndMetricQuantity(String name, double quantity);

    public List<Measurement> findByIdIn(List<Integer> ids);
}
