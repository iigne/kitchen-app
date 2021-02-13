package com.kitchenapp.kitchenappapi.providers.model


import com.kitchenapp.kitchenappapi.model.Recipe
import com.kitchenapp.kitchenappapi.providers.CommonTestData

class RecipeProvider {

    static Map DEFAULTS = [
            id               : CommonTestData.RECIPE_ID,
            title            : "New recipe",
            imageLink        : "https://potato.com/potato.jpeg",
            method           : "Step 1: cook food; Step 2: eat food",
            recipeIngredients: [],
            author           : UserProvider.make(),
            users            : [UserProvider.make()]
    ]

    static Recipe make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new Recipe([
                id               : props.id,
                title            : props.title,
                imageLink        : props.imageLink,
                method           : props.method,
                recipeIngredients: props.recipeIngredients,
                author           : props.author,
                users            : props.users,
        ])
    }
}
