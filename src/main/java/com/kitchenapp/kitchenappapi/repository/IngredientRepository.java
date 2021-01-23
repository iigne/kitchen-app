package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    List<Ingredient> findByNameContains(String term);
}
