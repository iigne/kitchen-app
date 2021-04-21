package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    List<Ingredient> findTop5ByNameContains(String term);

    Optional<Ingredient> findByName(String name);

}
