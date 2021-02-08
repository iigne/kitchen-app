package com.kitchenapp.kitchenappapi.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class QuantityDTO {
    private Integer measurementId;
    private double quantity;
    private String measurementName;
}
