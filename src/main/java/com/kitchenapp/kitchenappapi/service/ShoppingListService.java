package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.request.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.mapper.ShoppingListMapper;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.ShoppingUserIngredient;
import com.kitchenapp.kitchenappapi.model.User;
import com.kitchenapp.kitchenappapi.repository.ShoppingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    private final MeasurementService measurementService;
    private final UserService userService;
    private final IngredientService ingredientService;

    public List<ShoppingUserIngredient> findAllByUser(final int userId) {
        return shoppingListRepository.findAllByUserId(userId);
    }

    public ShoppingUserIngredient createItemForUser(IngredientQuantityDTO item, final int userId) {
        Ingredient ingredient = ingredientService.findByIdOrThrow(item.getIngredientId());
        User user = userService.findByIdOrThrow(userId);
        Measurement measurement = measurementService.findByIdOrThrow(item.getMeasurementId());
        return shoppingListRepository.save(ShoppingListMapper.toEntity(ingredient, user, measurement, item.getQuantity()));
    }

    public void deleteAllByUser(final int userId) {
        shoppingListRepository.deleteAllByUserId(userId);
    }



}
