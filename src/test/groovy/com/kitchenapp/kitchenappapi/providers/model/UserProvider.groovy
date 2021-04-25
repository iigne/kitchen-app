package com.kitchenapp.kitchenappapi.providers.model


import com.kitchenapp.kitchenappapi.model.user.User
import com.kitchenapp.kitchenappapi.providers.CommonTestData

class UserProvider {

    static Map DEFAULTS = [
            id: CommonTestData.USER_ID,
            username: "user",
            email: "email@test.com",
            password: "passwordverystrong123",
            userIngredients: [],
            userRecipes: []
    ]

    static User make(Map overrides = [:]) {
        def props = DEFAULTS + overrides

        return new User([
                id: props.id,
                username: props.username,
                email: props.email,
                password: props.password,
                userIngredients: props.userIngredients,
                userRecipes: props.userRecipes
        ])
    }
}
