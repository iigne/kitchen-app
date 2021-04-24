package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.dto.QuantityDTO;
import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO;
import com.kitchenapp.kitchenappapi.dto.recipe.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.mapper.UserIngredientMapper;
import com.kitchenapp.kitchenappapi.model.JwtUserDetails;
import com.kitchenapp.kitchenappapi.service.UserIngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.kitchenapp.kitchenappapi.mapper.UserIngredientMapper.toDTO;

@Slf4j
@RestController
@RequestMapping("/user-ingredient")
@RequiredArgsConstructor
public class UserIngredientController {

    private final UserIngredientService userIngredientService;

    @GetMapping
    public ResponseEntity<List<UserIngredientDTO>> getByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(
                UserIngredientMapper.toDTO(userIngredientService.listAllForUser(userDetails.getId())));
    }

    @PostMapping
    public ResponseEntity<UserIngredientDTO> create(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                    @RequestBody UserIngredientDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                toDTO(userIngredientService.create(userDetails.getId(), dto))
        );
    }

    @PatchMapping("/quantity")
    public ResponseEntity<UserIngredientDTO> updateQuantity(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                            @RequestParam int ingredientId,
                                                            @RequestBody @NotNull QuantityDTO quantityDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                UserIngredientMapper.toDTO(userIngredientService.updateQuantity(userDetails.getId(), ingredientId, quantityDTO))
        );
    }

    @PatchMapping("/remove-quantities")
    public ResponseEntity<List<UserIngredientDTO>> removeQuantities(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                                    @RequestBody List<IngredientQuantityDTO> ingredientQuantities) {
        return ResponseEntity.status(HttpStatus.OK).body(
                toDTO(userIngredientService.updateQuantities(userDetails.getId(), ingredientQuantities))
        );
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal JwtUserDetails userDetails,
                                    @RequestParam int ingredientId) {
        userIngredientService.delete(userDetails.getId(), ingredientId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
