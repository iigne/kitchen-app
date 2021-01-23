package com.kitchenapp.kitchenappapi.model;

import javax.persistence.*;

@Entity
public class Measurement {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String name;

    private int metricQuantity;

    @Enumerated(EnumType.STRING)
    private MetricUnit metricUnit;
}
