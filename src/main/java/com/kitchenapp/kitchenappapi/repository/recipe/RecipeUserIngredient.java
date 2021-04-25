package com.kitchenapp.kitchenappapi.repository.recipe;

/**
 * Interface-based projection for fetching results from custom SQL queries
 */
public interface RecipeUserIngredient {
    int getRecipeId();
    int getIngredientId();
    String getIngredientName();
    Double getRecipeQuantityMetric();
    Double getUserQuantityMetric();
}
