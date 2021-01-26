package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.IngredientDTO;
import com.kitchenapp.kitchenappapi.mapper.IngredientMapper;
import com.kitchenapp.kitchenappapi.mapper.MeasurementMapper;
import com.kitchenapp.kitchenappapi.model.Category;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.MetricUnit;
import com.kitchenapp.kitchenappapi.repository.CategoryRepository;
import com.kitchenapp.kitchenappapi.repository.IngredientRepository;
import com.kitchenapp.kitchenappapi.repository.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;
    private final MeasurementRepository measurementRepository;

    public Ingredient create(IngredientDTO ingredientDTO) {
        //TODO handle when ingredient exists
        //TODO handle with ApiError
        Category category = categoryRepository.findByName(ingredientDTO.getCategory()).orElseThrow(IllegalStateException::new);
        Set<Measurement> measurements = ingredientDTO.getMeasurements().stream()
                .map(m -> measurementRepository.findByNameAndMetricQuantityAndMetricUnit(
                        m.getName(), m.getMetricQuantity(), MetricUnit.get(m.getMetricUnit()))
                        .orElse(MeasurementMapper.toEntity(m)))
                .collect(Collectors.toSet());
        return ingredientRepository.save(IngredientMapper.toEntity(ingredientDTO, category, measurements));
    }
}
