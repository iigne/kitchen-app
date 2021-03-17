package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.ShoppingUserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository extends JpaRepository<ShoppingUserIngredient, Integer> {

    List<ShoppingUserIngredient> findAllByUserId(final int userId);

    Optional<ShoppingUserIngredient> findByUserIdAndIngredientId(final int userId, final int ingredientId);

    @Modifying
    @Transactional
    void deleteAllByIngredientIdInAndUserId(final List<Integer> ingredientIds, final int userId);

    @Modifying
    @Transactional
    void deleteAllByUserId(final int userId);

}
