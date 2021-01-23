package com.kitchenapp.kitchenappapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IngredientDTO {
    @NotBlank
    private String name;

    @NotBlank
    @Size(max=2)
    private String metricUnit;

    @NotBlank
    private String category;

    private int shelfLifeDays;

    private List<MeasurementDTO> measurements;
}
