package com.kitchenapp.kitchenappapi.mapper.ingredient;

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientDTO;
import com.kitchenapp.kitchenappapi.model.ingredient.Category;
import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient;
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import com.kitchenapp.kitchenappapi.model.ingredient.MetricUnit;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static IngredientDTO toDTO(Ingredient entity) {
        return IngredientDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .metricUnit(entity.getMetricUnit().name())
                .category(entity.getCategory().getName())
                .shelfLifeDays(entity.getShelfLifeDays())
                .measurements(MeasurementMapper.toDTO(entity.getMeasurements()))
                .build();
    }

    public static List<IngredientDTO> toDTOs(List<Ingredient> entities) {
        return entities.stream().map(IngredientMapper::toDTO).collect(Collectors.toList());
    }
}
