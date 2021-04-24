package com.kitchenapp.kitchenappapi.repository.recipe;

public interface RecipeUserIngredient {
    int getRecipeId();
    int getIngredientId();
    String getIngredientName();
    Double getRecipeQuantityMetric();
    Double getUserQuantityMetric();
}
