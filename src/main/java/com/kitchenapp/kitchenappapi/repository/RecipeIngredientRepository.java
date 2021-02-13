package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {

}
