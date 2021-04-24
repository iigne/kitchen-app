package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.dto.ShoppingListItemDTO;
import com.kitchenapp.kitchenappapi.dto.recipe.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.mapper.ShoppingListMapper;
import com.kitchenapp.kitchenappapi.model.JwtUserDetails;
import com.kitchenapp.kitchenappapi.model.ShoppingUserIngredient;
import com.kitchenapp.kitchenappapi.service.ShoppingListService;
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
@RequestMapping("/shopping")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    @GetMapping
    public ResponseEntity<List<ShoppingListItemDTO>> getListByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        List<ShoppingUserIngredient> ingredients = shoppingListService.findAllByUserId(userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ShoppingListMapper.toDTOs(ingredients));
    }

    @PostMapping
    public ResponseEntity<ShoppingListItemDTO> createListItem(@RequestBody @Valid IngredientQuantityDTO item,
                                                              @AuthenticationPrincipal JwtUserDetails userDetails) {
        ShoppingUserIngredient ingredient = shoppingListService.create(userDetails.getId(), item);
        return ResponseEntity.status(HttpStatus.CREATED).body(ShoppingListMapper.toDTO(ingredient));
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<ShoppingListItemDTO>> createMultipleListItems(@RequestBody @Valid List<IngredientQuantityDTO> items,
                                                                      @AuthenticationPrincipal JwtUserDetails userDetails) {

        List<ShoppingUserIngredient> ingredients = shoppingListService.createItemsForUser(items, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ShoppingListMapper.toDTOs(ingredients));
    }

    @PatchMapping
    public ResponseEntity<ShoppingListItemDTO> updateListItem(@RequestBody @Valid IngredientQuantityDTO item,
                                                              @AuthenticationPrincipal JwtUserDetails userDetails) {
        ShoppingUserIngredient ingredient = shoppingListService.update(userDetails.getId(), item);
        return ResponseEntity.status(HttpStatus.OK).body(ShoppingListMapper.toDTO(ingredient));
    }

    @PostMapping("/tick")
    public ResponseEntity<Map<String, Boolean>> updateItemTick(@RequestParam final int ingredientId,
                                                               @AuthenticationPrincipal JwtUserDetails userDetails) {
        boolean ticked = shoppingListService.addOrRemoveTick(ingredientId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("ticked", ticked));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteItem(@RequestParam final int ingredientId, @AuthenticationPrincipal JwtUserDetails userDetails) {
        shoppingListService.deleteByIngredientAndUserId(ingredientId, userDetails.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/multiple")
    public ResponseEntity<?> clearList(@AuthenticationPrincipal JwtUserDetails userDetails) {
        shoppingListService.deleteAllByUser(userDetails.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PostMapping("/clear-and-import")
    public ResponseEntity<List<ShoppingListItemDTO>> clearAndImportTickedItems(@AuthenticationPrincipal JwtUserDetails userDetails) {
        shoppingListService.clearAndImport(userDetails.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
