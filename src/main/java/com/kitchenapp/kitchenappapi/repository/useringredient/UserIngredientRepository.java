package com.kitchenapp.kitchenappapi.repository.useringredient;

import com.kitchenapp.kitchenappapi.model.useringredient.UserIngredient;
import com.kitchenapp.kitchenappapi.model.useringredient.UserIngredientId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserIngredientRepository extends AbstractUserIngredientRepository<UserIngredient, UserIngredientId> {

    List<UserIngredient> findAllByUserIdAndIngredientIdIn(int userId, List<Integer> ingredientIds);

    @Transactional
    @Modifying
    void deleteByIngredientIdAndUserId(int ingredientId, int userId);

}
