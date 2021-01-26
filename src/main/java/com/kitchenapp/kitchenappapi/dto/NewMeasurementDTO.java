package com.kitchenapp.kitchenappapi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewMeasurementDTO {
    private String name;
    private String metricUnit;
    private int metricQuantity;
}
