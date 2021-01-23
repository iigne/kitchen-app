package com.kitchenapp.kitchenappapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "ingredient")
public class Ingredient {

    @Id
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MetricUnit metricUnit;

    //TODO could be duration?
    private int shelfLifeDays;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ingredient_measurement",
            joinColumns = {@JoinColumn(name = "ingredient_id")},
            inverseJoinColumns = {@JoinColumn(name = "measurement_id")}
    )
    private Set<Measurement> measurements;
}
