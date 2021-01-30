package com.kitchenapp.kitchenappapi.helper;

import com.kitchenapp.kitchenappapi.model.Measurement;

import java.text.DecimalFormat;

public class MeasurementConverter {

    static final DecimalFormat df = new DecimalFormat("#.00");

    public static double toMetric(double quantity, Measurement measurement) {
        return Double.parseDouble(df.format(measurement.getMetricQuantity() * quantity));
    }

    public static double toMeasurement(double metricQuantity, Measurement measurement) {
        return Double.parseDouble(df.format(metricQuantity/measurement.getMetricQuantity()));
    }

}
