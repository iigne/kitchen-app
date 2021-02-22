package com.kitchenapp.kitchenappapi.model;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String name;

    //TODO not necessary anymore?
    @Enumerated(EnumType.STRING)
    private MetricUnit metricUnit;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    private int shelfLifeDays;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "ingredient_measurement",
            joinColumns = {@JoinColumn(name = "ingredient_id")},
            inverseJoinColumns = {@JoinColumn(name = "measurement_id")}
    )
    private Set<Measurement> measurements;

    @JsonIgnore
    public Measurement getMetricMeasurement() {
        if(measurements == null || measurements.isEmpty()) {
            throw new IllegalStateException("Metric measurement not found");
        }
        return measurements.stream().filter(Measurement::isMetric).collect(Collectors.toList()).get(0);
    }
}
