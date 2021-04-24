package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.UserIngredient;
import com.kitchenapp.kitchenappapi.model.UserIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserIngredientRepository extends AbstractUserIngredientRepository<UserIngredient, UserIngredientId> {

    List<UserIngredient> findAllByUserIdAndIngredientIdIn(int userId, List<Integer> ingredientIds);

    @Transactional
    @Modifying
    void deleteByIngredientIdAndUserId(int ingredientId, int userId);

}
