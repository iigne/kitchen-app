package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.dto.ShoppingListItemDTO;
import com.kitchenapp.kitchenappapi.dto.request.IngredientQuantityDTO;
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
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/shopping")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    @GetMapping("/list")
    public ResponseEntity<List<ShoppingListItemDTO>> getListByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        List<ShoppingUserIngredient> ingredients = shoppingListService.findAllByUser(userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ShoppingListMapper.toDTOs(ingredients));
    }

    @DeleteMapping("/list/clear")
    public ResponseEntity<?> clearList(@AuthenticationPrincipal JwtUserDetails userDetails) {
        shoppingListService.deleteAllByUser(userDetails.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //this will be used for removing ticked off items from user's list
    //and importing to user's stock
    @DeleteMapping("/list/clear-and-import") //TODO rename this is horrible
    public ResponseEntity<List<ShoppingListItemDTO>> clearItemsFromList(List<Integer> ingredientIds, @AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/item")
    public ResponseEntity<ShoppingListItemDTO> createListItem(@RequestBody @Valid IngredientQuantityDTO item,
                                                              @AuthenticationPrincipal JwtUserDetails userDetails) {
        ShoppingUserIngredient ingredient = shoppingListService.createItemForUser(item, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ShoppingListMapper.toDTO(ingredient));
    }

    @PatchMapping("/item")
    public ResponseEntity<ShoppingListItemDTO> updateListItem(IngredientQuantityDTO ingredient, @AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/item/tick")
    public ResponseEntity<Map<String, Boolean>> updateItemTick(@RequestParam final int ingredientId, @AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/item")
    public ResponseEntity<?> deleteItem(@RequestParam final int ingredientId, @AuthenticationPrincipal JwtUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
