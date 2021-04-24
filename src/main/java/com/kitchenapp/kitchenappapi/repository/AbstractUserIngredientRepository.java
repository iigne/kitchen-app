package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.AbstractUserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface AbstractUserIngredientRepository<T extends AbstractUserIngredient, UserIngredientId> extends JpaRepository<T, UserIngredientId> {

    List<T> findAllByUserId(final int userId);

    Optional<T> findByUserIdAndIngredientId(final int userId, final int ingredientId);

}
