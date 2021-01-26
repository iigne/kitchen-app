package com.kitchenapp.kitchenappapi.mapper;

import com.kitchenapp.kitchenappapi.dto.IngredientDTO;
import com.kitchenapp.kitchenappapi.model.Category;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.MetricUnit;

import java.util.List;
import java.util.Set;

public class IngredientMapper {

    public static Ingredient toEntity(IngredientDTO dto, Category category, Set<Measurement> measurements) {
        return Ingredient.builder()
                .name(dto.getName())
                .category(category)
                .metricUnit(MetricUnit.get(dto.getMetricUnit()))
                .shelfLifeDays(dto.getShelfLifeDays() != null ? dto.getShelfLifeDays() : category.getShelfLifeDays())
                .measurements(measurements)
                .build();
    }
}
