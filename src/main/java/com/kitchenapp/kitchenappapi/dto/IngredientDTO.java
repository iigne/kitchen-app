package com.kitchenapp.kitchenappapi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IngredientDTO {

    private int id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max=2)
    private String metricUnit;

    @NotBlank
    private String category;

    private Integer shelfLifeDays;

    private List<MeasurementDTO> measurements;
}
