package com.kitchenapp.kitchenappapi.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "ingredient")
public class Ingredient {

    @Id
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MetricUnit metricUnit;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    private int shelfLifeDays;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ingredient_measurement",
            joinColumns = {@JoinColumn(name = "ingredient_id")},
            inverseJoinColumns = {@JoinColumn(name = "measurement_id")}
    )
    private Set<Measurement> measurements;
}
