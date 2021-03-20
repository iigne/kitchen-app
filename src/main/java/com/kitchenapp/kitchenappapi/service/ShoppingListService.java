package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.request.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.mapper.ShoppingListMapper;
import com.kitchenapp.kitchenappapi.mapper.UserIngredientMapper;
import com.kitchenapp.kitchenappapi.model.*;
import com.kitchenapp.kitchenappapi.repository.ShoppingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    private final MeasurementService measurementService;
    private final UserService userService;
    private final IngredientService ingredientService;
    private final UserIngredientService userIngredientService;

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

    //TODO maybe we want this as transactional
    public void clearAndImport(final int userId) {

        //TODO this definitely needs a test
        List<ShoppingUserIngredient> shoppingIngredients = getTickedByUser(userId);
        List<Integer> ingredientIds = shoppingIngredients.stream().map(i -> i.getId().getIngredientId()).collect(Collectors.toList());
        List<UserIngredient> userIngredients = userIngredientService.getByIds(ingredientIds, userId);

        List<UserIngredient> updatedIngredients = extractUserIngredientsFromShopping(shoppingIngredients, userIngredients);
        userIngredientService.saveAll(updatedIngredients);
        shoppingListRepository.deleteAll(shoppingIngredients);
    }

    public List<UserIngredient> extractUserIngredientsFromShopping(List<ShoppingUserIngredient> shoppingUserIngredients,
                                                                   List<UserIngredient> userIngredients) {
        Map<Integer, ShoppingUserIngredient> shoppingMap = shoppingUserIngredients.stream().collect(
                Collectors.toMap(i -> i.getId().getIngredientId(), Function.identity()));
        Map<Integer, UserIngredient> userIngredientMap = userIngredients.stream().collect(
                Collectors.toMap(i -> i.getId().getIngredientId(), Function.identity()));

        List<UserIngredient> toSave = new ArrayList<>();
        for(Integer key : shoppingMap.keySet()) {
            UserIngredient userIngredient = userIngredientMap.get(key);
            ShoppingUserIngredient shoppingIngredient = shoppingMap.get(key);
            if(userIngredient != null) {
                double currentQuantity = userIngredient.getMetricQuantity();
                double shoppingQuantity = shoppingIngredient.getMetricQuantity();
                userIngredient.setMetricQuantity(currentQuantity + shoppingQuantity);
                toSave.add(userIngredient);
            } else {
                toSave.add(UserIngredientMapper.toEntity(shoppingIngredient));
            }
        }
        return toSave;
    }

    public ShoppingUserIngredient getByIdOrThrow(final int ingredientId, final int userId) {
        return shoppingListRepository.findByUserIdAndIngredientId(userId, ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("userId %s and ingredientId %s doesn't exist", userId, ingredientId)));
    }

    public List<ShoppingUserIngredient> getByIdsOrThrow(final List<Integer> ingredientIds, final int userId) {
        List<ShoppingUserIngredient> ingredients = shoppingListRepository.findAllByUserIdAndIngredientIdIn(userId, ingredientIds);
        if(ingredientIds.size() > ingredients.size()) {
            throw new EntityNotFoundException("Given ingredientIds were not all found in the database");
        }
        return ingredients;
    }

    public List<ShoppingUserIngredient> getTickedByUser(final int userId) {
        return shoppingListRepository.findAllByUserIdAndTickedIsTrue(userId);
    }

    public void deleteAllByUser(final int userId) {
        shoppingListRepository.deleteAllByUserId(userId);
    }

    public void deleteByIngredientAndUserId(final int ingredientId, final  int userId) {
        ShoppingUserIngredient ingredient = getByIdOrThrow(ingredientId, userId);
        shoppingListRepository.delete(ingredient);
    }

}
