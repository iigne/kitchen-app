package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.dto.recipe.RequestRecipeDTO;
import com.kitchenapp.kitchenappapi.dto.recipe.ResponseRecipeDTO;
import com.kitchenapp.kitchenappapi.model.JwtUserDetails;
import com.kitchenapp.kitchenappapi.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/list")
    public ResponseEntity<List<ResponseRecipeDTO>> listAll(@RequestParam final String option, @AuthenticationPrincipal JwtUserDetails userDetails) {
        switch (option) {
            case "liked":
                return ResponseEntity.ok(recipeService.getAllLikedByUser(userDetails.getId()));
            case "created":
                return ResponseEntity.ok(recipeService.getAllCreatedByUser(userDetails.getId()));
            default:
                return ResponseEntity.ok(recipeService.getAllWithQuantities(userDetails.getId()));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseRecipeDTO> create(@RequestBody @Valid RequestRecipeDTO requestRecipeDTO, @AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recipeService.create(requestRecipeDTO, userDetails.getId()));
    }

    @PatchMapping
    public ResponseEntity<ResponseRecipeDTO> update(@RequestBody @Valid RequestRecipeDTO requestRecipeDTO, @AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(recipeService.update(requestRecipeDTO, userDetails.getId()));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteById(@RequestParam final int recipeId, @AuthenticationPrincipal JwtUserDetails userDetails) {
        recipeService.delete(recipeId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/like")
    public ResponseEntity<Map<String, Boolean>> updateUserRecipes(@RequestParam final int recipeId, @AuthenticationPrincipal JwtUserDetails userDetails) {
        boolean likeResult = recipeService.removeOrAddFromUserRecipes(recipeId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("liked", likeResult));
    }

}
