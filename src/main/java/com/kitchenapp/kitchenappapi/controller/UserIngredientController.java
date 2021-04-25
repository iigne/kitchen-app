package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.dto.useringredient.RequestUserIngredientDTO;
import com.kitchenapp.kitchenappapi.dto.useringredient.ResponseUserIngredientDTO;
import com.kitchenapp.kitchenappapi.mapper.useringredient.UserIngredientMapper;
import com.kitchenapp.kitchenappapi.model.user.JwtUserDetails;
import com.kitchenapp.kitchenappapi.service.useringredient.UserIngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kitchenapp.kitchenappapi.mapper.useringredient.UserIngredientMapper.toDTO;

@Slf4j
@RestController
@RequestMapping("/user-ingredient")
@RequiredArgsConstructor
public class UserIngredientController {

    private final UserIngredientService userIngredientService;

    @GetMapping
    public ResponseEntity<List<ResponseUserIngredientDTO>> getByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(
                UserIngredientMapper.toDTO(userIngredientService.findAllByUserId(userDetails.getId())));
    }

    @PostMapping
    public ResponseEntity<ResponseUserIngredientDTO> create(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                            @RequestBody RequestUserIngredientDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                toDTO(userIngredientService.create(userDetails.getId(), dto))
        );
    }

    @PatchMapping
    public ResponseEntity<ResponseUserIngredientDTO> update(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                            @RequestBody RequestUserIngredientDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(
                UserIngredientMapper.toDTO(userIngredientService.update(userDetails.getId(), dto))
        );
    }

    @PatchMapping("/remove-quantities")
    public ResponseEntity<List<ResponseUserIngredientDTO>> removeQuantities(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                                            @RequestBody List<IngredientQuantityDTO> ingredientQuantities) {
        return ResponseEntity.status(HttpStatus.OK).body(
                toDTO(userIngredientService.updateQuantities(userDetails.getId(), ingredientQuantities))
        );
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal JwtUserDetails userDetails,
                                    @RequestParam int ingredientId) {
        userIngredientService.deleteByIngredientAndUserId(ingredientId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
