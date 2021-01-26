package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.dto.IngredientDTO;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.JwtUserDetails;
import com.kitchenapp.kitchenappapi.repository.IngredientRepository;
import com.kitchenapp.kitchenappapi.service.IngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/all")
    public ResponseEntity<List<Ingredient>> getAll(){
        return ResponseEntity.ok(ingredientRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Ingredient> createIngredient(@AuthenticationPrincipal final JwtUserDetails userDetails,
                                                       @RequestBody @Valid IngredientDTO ingredientDTO) {
        try {
            Ingredient ingredient = ingredientService.create(ingredientDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(ingredient);
        } catch (Exception e) {
            log.error("action=create_ingredient user_id={} status=error", userDetails.getId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Ingredient>> searchByName(@RequestParam String term) {
        List<Ingredient> ingredients = ingredientRepository.findByNameContains(term);
        return ResponseEntity.ok().body(ingredients);
    }
}
