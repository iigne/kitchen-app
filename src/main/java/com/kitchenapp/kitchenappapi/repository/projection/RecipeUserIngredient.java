package com.kitchenapp.kitchenappapi.repository.projection;

public interface RecipeUserIngredient {
    int getRecipeId();
    int getIngredientId();
    String getIngredientName();
    Double getRecipeQuantityMetric();
    Double getUserQuantityMetric();
}
