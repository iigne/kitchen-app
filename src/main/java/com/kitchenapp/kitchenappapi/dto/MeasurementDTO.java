package com.kitchenapp.kitchenappapi.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MeasurementDTO {
    private int id;
    private String name;
    private String metricUnit;
    private double metricQuantity;
}
