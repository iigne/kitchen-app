package com.kitchenapp.kitchenappapi.model.ingredient;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer id;

    private String name;

    private double metricQuantity;

    @Enumerated(EnumType.STRING)
    private MetricUnit metricUnit;

    @JsonIgnore
    public boolean isMetric() {
        return name.equals(metricUnit.getAbbreviation());
    }
}
