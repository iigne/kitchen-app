package com.kitchenapp.kitchenappapi.service.ingredient;

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientDTO;
import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.mapper.ingredient.IngredientMapper;
import com.kitchenapp.kitchenappapi.mapper.ingredient.MeasurementMapper;
import com.kitchenapp.kitchenappapi.model.ingredient.Category;
import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient;
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import com.kitchenapp.kitchenappapi.model.ingredient.MetricUnit;
import com.kitchenapp.kitchenappapi.repository.ingredient.CategoryRepository;
import com.kitchenapp.kitchenappapi.repository.ingredient.IngredientRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;
    private final MeasurementService measurementService;


    /**
     * Creates a new ingredient with measurements.
     * A new ingredient always has at least one measurement (metric).
     *
     * @param ingredientDTO
     * @return
     */
    public Ingredient create(final IngredientDTO ingredientDTO) {
        Category category = categoryRepository.findByName(ingredientDTO.getCategory()).orElseThrow(IllegalStateException::new);
        Measurement measurement = measurementService.getMetricMeasurementOrThrow(MetricUnit.get(ingredientDTO.getMetricUnit()));
        Set<Measurement> measurements = ingredientDTO.getMeasurements().stream()
                .map(m -> measurementService.findByNameQuantityUnit(m.getName(), m.getMetricQuantity(), MetricUnit.get(m.getMetricUnit()))
                        .orElse(MeasurementMapper.toEntity(m)))
                .collect(Collectors.toSet());
        measurements.add(measurement);
        return ingredientRepository.save(IngredientMapper.toEntity(ingredientDTO, category, measurements));
    }

    public Ingredient findByIdOrThrow(final int id) {
        return ingredientRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("ingredientId %s not found", id)));
    }

    public Map<Integer, Ingredient> extractIngredientsFromDTOs(List<IngredientQuantityDTO> ingredientQuantityDTOs) {
        List<Integer> ingredientIds = ingredientQuantityDTOs.stream().map(IngredientQuantityDTO::getIngredientId).collect(Collectors.toList());
        List<Ingredient> ingredients = ingredientRepository.findAllByIdIn(ingredientIds);
        return ingredients.stream().collect(Collectors.toMap(Ingredient::getId, Function.identity()));
    }

}
