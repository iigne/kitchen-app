package com.kitchenapp.kitchenappapi.dto.useringredient;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ResponseQuantityDTO {
    private Integer measurementId;
    private String measurementName;
    private double quantity;
}
