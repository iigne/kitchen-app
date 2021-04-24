package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.RecipeIngredient;
import com.kitchenapp.kitchenappapi.repository.projection.RecipeUserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {

    @Query(value =
            "select ri.recipe_id as recipeId, i.id as ingredientId, i.name as ingredientName, ri.metric_quantity as recipeQuantityMetric, " +
                    "ui.metric_quantity as userQuantityMetric " +
            "from (select * from user_ingredient ui where ui.user_id = :userId) as ui\n" +
            "right join recipe_ingredient ri on ui.ingredient_id=ri.ingredient_id " +
            "join ingredient i on ri.ingredient_id = i.id"
            , nativeQuery = true)
    List<RecipeUserIngredient> fetchIngredientQuantitiesForAllRecipesByUserId(@Param("userId") int userId);

    @Query(value =
            "select ri.recipe_id as recipeId, i.id as ingredientId, i.name as ingredientName, ri.metric_quantity as recipeQuantityMetric, " +
                    "ui.metric_quantity as userQuantityMetric " +
                    "from (select * from user_ingredient ui where ui.user_id = :userId) as ui\n" +
                    "right join recipe_ingredient ri on ui.ingredient_id=ri.ingredient_id " +
                    "join ingredient i on ri.ingredient_id = i.id " +
                    "where ri.recipe_id = :recipeId"
            , nativeQuery = true)
    List<RecipeUserIngredient> fetchIngredientQuantitiesByUserIdAndRecipeId(@Param("userId") int userId, @Param("recipeId") int recipeId);

    @Query(value =
            "select ri.recipe_id as recipeId, i.id as ingredientId, i.name as ingredientName, ri.metric_quantity as recipeQuantityMetric, " +
                    "ui.metric_quantity as userQuantityMetric " +
                    "from (select * from user_ingredient ui where ui.user_id = :userId) as ui\n" +
                    "right join recipe_ingredient ri on ui.ingredient_id=ri.ingredient_id " +
                    "join ingredient i on ri.ingredient_id = i.id " +
                    "where ri.recipe_id in :recipeIds"
            , nativeQuery = true)
    List<RecipeUserIngredient> fetchIngredientQuantitiesByUserIdAndRecipeIdsIn(@Param("userId") int userId, @Param("recipeIds") List<Integer> recipeIds);
}
