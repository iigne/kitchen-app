package com.kitchenapp.kitchenappapi.helper;

import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.MetricUnit;

public class MeasurementConverter {

    public static int toMetric(int quantity, Measurement measurement) {
        return measurement.getMetricQuantity() * quantity;
    }

}
