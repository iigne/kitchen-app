package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientDTO;
import com.kitchenapp.kitchenappapi.mapper.ingredient.IngredientMapper;
import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient;
import com.kitchenapp.kitchenappapi.repository.ingredient.IngredientRepository;
import com.kitchenapp.kitchenappapi.service.ingredient.IngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ingredient")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientRepository ingredientRepository;
    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<IngredientDTO> createIngredient(@RequestBody @Valid IngredientDTO ingredientDTO) {
        Ingredient ingredient = ingredientService.create(ingredientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(IngredientMapper.toDTO(ingredient));
    }

    @GetMapping("/search")
    public ResponseEntity<List<IngredientDTO>> searchByName(@RequestParam String term) {
        List<Ingredient> ingredients = ingredientRepository.findTop5ByNameContains(term);
        return ResponseEntity.ok().body(IngredientMapper.toDTOs(ingredients));
    }
}
