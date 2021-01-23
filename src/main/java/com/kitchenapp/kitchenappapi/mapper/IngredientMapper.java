package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.dto.IngredientDTO;
import com.kitchenapp.kitchenappapi.model.Category;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.MetricUnit;

public class IngredientMapper {

    public static Ingredient toEntity(IngredientDTO dto, Category category) {
        return Ingredient.builder()
                .name(dto.getName())
                .category(category)
                .metricUnit(MetricUnit.get(dto.getMetricUnit()))
                .shelfLifeDays(dto.getShelfLifeDays())
                .measurements(MeasurementMapper.toEntity(dto.getMeasurements()))
                .build();

    }
}
