package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.ShoppingUserIngredient;
import com.kitchenapp.kitchenappapi.model.UserIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingListRepository extends AbstractUserIngredientRepository<ShoppingUserIngredient, UserIngredientId> {

    List<ShoppingUserIngredient> findAllByUserIdAndTickedIsTrue(final int userId);

    @Modifying
    @Transactional
    void deleteAllByIngredientIdInAndUserId(final List<Integer> ingredientIds, final int userId);

    @Modifying
    @Transactional
    void deleteAllByUserId(final int userId);

}
