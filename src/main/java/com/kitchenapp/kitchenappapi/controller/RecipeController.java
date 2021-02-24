package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.dto.request.RequestRecipeDTO;
import com.kitchenapp.kitchenappapi.dto.response.ResponseRecipeDTO;
import com.kitchenapp.kitchenappapi.mapper.RecipeMapper;
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

    //also TODO is it better practice to have a bunch of endpoints or one and pass in an option that is string and will need to be if elsed?

    private final RecipeService recipeService;

    @GetMapping("/list/all")
    public ResponseEntity<List<ResponseRecipeDTO>> listAll(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.ok(recipeService.getAllWithQuantities(userDetails.getId()));
    }

    //TODO
    @GetMapping("/list/suggestion")
    public ResponseEntity<List<ResponseRecipeDTO>> listSuggestions(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/list/liked")
    public ResponseEntity<List<ResponseRecipeDTO>> listAllLikedByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.ok(recipeService.getAllLikedByUser(userDetails.getId()));
    }

    @GetMapping("/list/created")
    public ResponseEntity<List<ResponseRecipeDTO>> listAllCreatedByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.ok(recipeService.getAllCreatedByUser(userDetails.getId()));
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
    public ResponseEntity<List<ResponseRecipeDTO>> updateUserRecipes(@RequestParam final int recipeId, @AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(recipeService.removeOrAddFromUserRecipes(recipeId, userDetails.getId()));
    }

}
