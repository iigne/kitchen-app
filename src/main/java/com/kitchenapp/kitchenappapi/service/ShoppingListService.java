package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.request.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.mapper.ShoppingListMapper;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.ShoppingUserIngredient;
import com.kitchenapp.kitchenappapi.model.User;
import com.kitchenapp.kitchenappapi.repository.ShoppingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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
        return shoppingListRepository.save(createFromDTO(item, userId));
    }

    public List<ShoppingUserIngredient> createItemsForUser(List<IngredientQuantityDTO> items, final int userId) {
        List<ShoppingUserIngredient> toSave = items.stream().map(i -> createFromDTO(i, userId)).collect(Collectors.toList());
        return shoppingListRepository.saveAll(toSave);
    }

    private ShoppingUserIngredient createFromDTO(IngredientQuantityDTO item, final int userId) {
        final int ingredientId = item.getIngredientId();

        shoppingListRepository.findByUserIdAndIngredientId(userId, ingredientId)
                .ifPresent(i -> {
                    throw new IllegalStateException(
                            String.format("userId %s and ingredientId %s already exists", userId, ingredientId)
                    );
                });

        Ingredient ingredient = ingredientService.findByIdOrThrow(item.getIngredientId());
        User user = userService.findByIdOrThrow(userId);
        Measurement measurement = measurementService.findByIdOrThrow(item.getMeasurementId());
        return ShoppingListMapper.toEntity(ingredient, user, measurement, item.getQuantity());
    }

    public ShoppingUserIngredient updateFromDTO(IngredientQuantityDTO item, final int userId) {
        final int ingredientId = item.getIngredientId();

        ShoppingUserIngredient ingredient = getByIdOrThrow(ingredientId, userId);

        final Integer measurementId = item.getMeasurementId();
        Measurement measurement = measurementService.findByIdOrThrow(measurementId);
        double metricQuantity =  MeasurementConverter.toMetricIfMetric(item.getQuantity(), measurement);
        ingredient.setMetricQuantity(metricQuantity);
        ingredient.setMeasurement(measurement);
        return shoppingListRepository.save(ingredient);
    }

    public boolean addOrRemoveTick(final int ingredientId, final int userId) {
        ShoppingUserIngredient ingredient = getByIdOrThrow(ingredientId, userId);
        boolean tickedValue = ingredient.isTicked();
        ingredient.setTicked(!tickedValue);
        return shoppingListRepository.save(ingredient).isTicked();
    }

    public ShoppingUserIngredient getByIdOrThrow(final int ingredientId, final int userId) {
        return shoppingListRepository.findByUserIdAndIngredientId(userId, ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("userId %s and ingredientId %s doesn't exist", userId, ingredientId)));
    }

    public void deleteAllByUser(final int userId) {
        shoppingListRepository.deleteAllByUserId(userId);
    }

    public void deleteByIngredientAndUserId(final int ingredientId, final  int userId) {
        ShoppingUserIngredient ingredient = getByIdOrThrow(ingredientId, userId);
        shoppingListRepository.delete(ingredient);
    }

}
