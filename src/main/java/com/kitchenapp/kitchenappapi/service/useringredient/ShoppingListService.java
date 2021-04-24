package com.kitchenapp.kitchenappapi.service.useringredient;

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.mapper.useringredient.ShoppingListMapper;
import com.kitchenapp.kitchenappapi.mapper.useringredient.UserIngredientMapper;
import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient;
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement;
import com.kitchenapp.kitchenappapi.model.useringredient.ShoppingUserIngredient;
import com.kitchenapp.kitchenappapi.model.useringredient.UserIngredient;
import com.kitchenapp.kitchenappapi.model.user.User;
import com.kitchenapp.kitchenappapi.repository.useringredient.ShoppingListRepository;
import com.kitchenapp.kitchenappapi.service.ingredient.IngredientService;
import com.kitchenapp.kitchenappapi.service.ingredient.MeasurementService;
import com.kitchenapp.kitchenappapi.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShoppingListService extends AbstractUserIngredientService<ShoppingUserIngredient, ShoppingListRepository, IngredientQuantityDTO> {

    private final ShoppingListRepository shoppingListRepository;
    private final UserIngredientService userIngredientService;

    @Override
    protected ShoppingListRepository getRepository() {
        return shoppingListRepository;
    }

    @Override
    protected ShoppingUserIngredient mapToEntity(IngredientQuantityDTO dto, Ingredient ingredient, User user, Measurement measurement) {
        return ShoppingListMapper.toEntity(ingredient, user, measurement, dto.getQuantity());
    }

    public ShoppingListService(ShoppingListRepository shoppingListRepository, MeasurementService measurementService, UserService userService,
                               IngredientService ingredientService, UserIngredientService userIngredientService) {
        super(measurementService, userService, ingredientService);
        this.shoppingListRepository = shoppingListRepository;
        this.userIngredientService = userIngredientService;
    }

    public List<ShoppingUserIngredient> createItemsForUser(List<IngredientQuantityDTO> items, final int userId) {
        List<ShoppingUserIngredient> toSave = items.stream().map(i -> create(userId, i)).collect(Collectors.toList());
        return shoppingListRepository.saveAll(toSave);
    }

    public boolean addOrRemoveTick(final int ingredientId, final int userId) {
        ShoppingUserIngredient ingredient = getByIngredientIdAndUserIdOrThrow(ingredientId, userId);
        boolean tickedValue = ingredient.isTicked();
        ingredient.setTicked(!tickedValue);
        return shoppingListRepository.save(ingredient).isTicked();
    }

    //TODO maybe we want this as transactional
    public void clearAndImport(final int userId) {

        List<ShoppingUserIngredient> shoppingIngredients = getTickedByUser(userId);
        List<Integer> ingredientIds = shoppingIngredients.stream().map(i -> i.getIngredient().getId()).collect(Collectors.toList());
        List<UserIngredient> userIngredients = userIngredientService.getByIds(ingredientIds, userId);

        List<UserIngredient> updatedIngredients = extractUserIngredientsFromShopping(shoppingIngredients, userIngredients);
        userIngredientService.saveAll(updatedIngredients);
        shoppingListRepository.deleteAll(shoppingIngredients);
    }

    public List<UserIngredient> extractUserIngredientsFromShopping(List<ShoppingUserIngredient> shoppingUserIngredients,
                                                                   List<UserIngredient> userIngredients) {
        Map<Integer, ShoppingUserIngredient> shoppingMap = shoppingUserIngredients.stream().collect(
                Collectors.toMap(i -> i.getIngredient().getId(), Function.identity()));
        Map<Integer, UserIngredient> userIngredientMap = userIngredients.stream().collect(
                Collectors.toMap(i -> i.getIngredient().getId(), Function.identity()));

        List<UserIngredient> toSave = new ArrayList<>();
        for (Integer key : shoppingMap.keySet()) {
            UserIngredient userIngredient = userIngredientMap.get(key);
            ShoppingUserIngredient shoppingIngredient = shoppingMap.get(key);
            if (userIngredient != null) {
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

    public List<ShoppingUserIngredient> getTickedByUser(final int userId) {
        return shoppingListRepository.findAllByUserIdAndTickedIsTrue(userId);
    }

    public void deleteAllByUser(final int userId) {
        shoppingListRepository.deleteAllByUserId(userId);
    }

}
