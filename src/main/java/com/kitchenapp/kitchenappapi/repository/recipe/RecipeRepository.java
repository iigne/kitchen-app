package com.kitchenapp.kitchenappapi.repository.recipe;

import com.kitchenapp.kitchenappapi.model.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    List<Recipe> findAllByAuthorId(int userId);

}

