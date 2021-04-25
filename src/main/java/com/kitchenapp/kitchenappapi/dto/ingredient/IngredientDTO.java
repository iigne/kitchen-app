package com.kitchenapp.kitchenappapi.dto.ingredient;

import lombok.*;

import javax.validation.constraints.NotBlank;
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
    private String metricUnit;

    @NotBlank
    private String category;

    private Integer shelfLifeDays;

    private List<MeasurementDTO> measurements;
}
