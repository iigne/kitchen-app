package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.dto.RecipeDTO;
import com.kitchenapp.kitchenappapi.model.JwtUserDetails;
import com.kitchenapp.kitchenappapi.model.Recipe;
import com.kitchenapp.kitchenappapi.service.RecipeService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/all")
    public ResponseEntity<List<Recipe>> listAll() {
        return ResponseEntity.ok(recipeService.getAll());
    }

    @GetMapping
    public ResponseEntity<Recipe> getById(@RequestParam final int recipeId) {
        return ResponseEntity.ok(recipeService.getByIdOrThrow(recipeId));
    }

    @GetMapping("/user")
    public ResponseEntity<Set<Recipe>> listAllByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.ok(recipeService.getAllByUser(userDetails.getId()));
    }

    @PostMapping
    public ResponseEntity<Recipe> create(@RequestBody @Valid RecipeDTO recipeDTO, @AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.create(recipeDTO, userDetails.getId()));
    }

}
