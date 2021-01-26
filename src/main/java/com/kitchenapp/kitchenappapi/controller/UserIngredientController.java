package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.JwtUserDetails;
import com.kitchenapp.kitchenappapi.model.UserIngredient;
import com.kitchenapp.kitchenappapi.service.UserIngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user-ingredient")
@RequiredArgsConstructor
public class UserIngredientController {

    private final UserIngredientService userIngredientService;

    @GetMapping
    public ResponseEntity<List<UserIngredientDTO>> getByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(
                userIngredientService.listAllForUser(userDetails.getId()));
    }

    @PostMapping
    public ResponseEntity<UserIngredientDTO> create(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                    @RequestBody UserIngredientDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userIngredientService.create(userDetails.getId(), dto)
        );
    }

    @PatchMapping
    public ResponseEntity<UserIngredientDTO> update(@RequestBody UserIngredientDTO dto) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    //TODO ?
    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal JwtUserDetails userDetails,
                                    @RequestParam int ingredientId) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
