package com.kitchenapp.kitchenappapi.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Measurement {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String name;

    private int metricQuantity;

    @Enumerated(EnumType.STRING)
    private MetricUnit metricUnit;
}
