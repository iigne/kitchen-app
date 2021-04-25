package com.kitchenapp.kitchenappapi.helper;

import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;

import java.text.DecimalFormat;

public class MeasurementConverter {

    static final DecimalFormat df = new DecimalFormat("#.00");

    public static double toMetric(double quantity, Measurement measurement) {
        return Double.parseDouble(df.format(measurement.getMetricQuantity() * quantity));
    }

    public static double toMetricIfMetric(double quantity, Measurement measurement) {
        return measurement.isMetric() ? quantity : toMetric(quantity, measurement);
    }

    public static double toMeasurement(double metricQuantity, Measurement measurement) {
        return Double.parseDouble(df.format(metricQuantity/measurement.getMetricQuantity()));
    }

    public static double toMeasurement(double originalQuantity, Measurement originalMeasurement, Measurement newMeasurement) {
        final double metricQuantity = toMetricIfMetric(originalQuantity, originalMeasurement);
        return toMeasurement(metricQuantity, newMeasurement);
    }

}
