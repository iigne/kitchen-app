package com.kitchenapp.kitchenappapi.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuantityDTO {
    private int measurementId;
    private int quantity;
    private String measurementName;
}
