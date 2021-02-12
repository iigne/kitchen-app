package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

}

