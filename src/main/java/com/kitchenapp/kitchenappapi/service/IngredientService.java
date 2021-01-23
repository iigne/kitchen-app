package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.IngredientDTO;
import com.kitchenapp.kitchenappapi.mapper.IngredientMapper;
import com.kitchenapp.kitchenappapi.model.Category;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.repository.CategoryRepository;
import com.kitchenapp.kitchenappapi.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;

    public Ingredient create(IngredientDTO ingredientDTO) {
        //TODO change exception here to ApiException when that is implemented
        //TODO handle when an ingredient already exists?
        Category category = categoryRepository.findByName(ingredientDTO.getCategory()).orElseThrow(IllegalStateException::new);
        return ingredientRepository.save(IngredientMapper.toEntity(ingredientDTO, category));
    }
}
