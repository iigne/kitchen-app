package com.kitchenapp.kitchenappapi.repository.ingredient;

import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    List<Ingredient> findTop5ByNameContains(String term);

    Optional<Ingredient> findByName(String name);

    List<Ingredient> findAllByIdIn(List<Integer> ingredientIds);
}
