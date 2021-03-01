package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.UserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserIngredientRepository extends JpaRepository<UserIngredient, Integer> {

    Optional<UserIngredient> findByUserIdAndIngredientId(int userId, int ingredientId);

    List<UserIngredient> findAllByUserId(int userId);

    List<UserIngredient> findAllByUserIdAndIngredientIdIn(int userId, Set<Integer> ingredientIds);

    @Transactional
    @Modifying
    void deleteByIngredientIdAndUserId(int ingredientId, int userId);

}
