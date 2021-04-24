package com.kitchenapp.kitchenappapi.dto.ingredient;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MeasurementDTO {
    private Integer id;
    private String name;
    private String metricUnit;
    private double metricQuantity;
}
