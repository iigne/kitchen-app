package com.kitchenapp.kitchenappapi.repository

import com.kitchenapp.kitchenappapi.providers.model.*
import com.kitchenapp.kitchenappapi.repository.ingredient.IngredientRepository
import com.kitchenapp.kitchenappapi.repository.recipe.RecipeIngredientRepository
import com.kitchenapp.kitchenappapi.repository.recipe.RecipeRepository
import com.kitchenapp.kitchenappapi.repository.user.UserRepository
import com.kitchenapp.kitchenappapi.repository.useringredient.UserIngredientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
@ActiveProfiles("test")
class RecipeRepositoryIntegrationSpec extends Specification {

    @Autowired
    RecipeIngredientRepository recipeIngredientRepository

    @Autowired
    RecipeRepository recipeRepository

    @Autowired
    IngredientRepository ingredientRepository

    @Autowired
    UserIngredientRepository userIngredientRepository

    @Autowired
    UserRepository userRepository

    @Unroll
    def "should retrieve user and recipe quantities of recipe ingredients for a single recipe"() {
        given: "ingredients exist in the database"
        def ingredient1 = ingredientRepository.save(IngredientProvider.make(id: 88, name: ingredient1name))
        def ingredient2 = ingredientRepository.save(IngredientProvider.make(id: 99, name: ingredient2name))

        and: "user who has ingredients exists in the database"
        def user = userRepository.save(UserProvider.make())
        def userIngredient1 = UserIngredientProvider.make(ingredient: ingredient1, metricQuantity: ingredient1user, user: user)

        userIngredientRepository.save(userIngredient1)

        and: "recipe with ingredients exists in the database"
        def recipe = RecipeProvider.make(name: "Pesto pasta", author: user, users: [user])
        def recipeIngredient1 = RecipeIngredientProvider.make(ingredient: ingredient1, metricQuantity: ingredient1recipe, recipe: recipe)
        def recipeIngredient2 = RecipeIngredientProvider.make(ingredient: ingredient2, metricQuantity: ingredient2recipe, recipe: recipe)
        recipe.setRecipeIngredients([recipeIngredient1, recipeIngredient2] as Set)
        recipeRepository.save(recipe)

        when: "repository method is called"
        def result = recipeIngredientRepository.fetchIngredientQuantitiesForAllRecipesByUserId(user.id)

        then: "correct data is returned"
        result.size() == 2
        result*.ingredientId.sort() == [ingredient1.id, ingredient2.id].sort()
        result*.ingredientName.sort() == [ingredient1name, ingredient2name].sort()
        result*.recipeQuantityMetric.sort() == [ingredient1recipe, ingredient2recipe].sort()
        result*.userQuantityMetric.sort() == [ingredient1user, ingredient2user].sort()

        where:
        ingredient1name | ingredient2name | ingredient1recipe | ingredient1user | ingredient2recipe | ingredient2user
        "Pesto"         | "Pasta"         | 25.0              | 50.0            | 250.0             | null
    }

    def cleanup() {
        userIngredientRepository.deleteAll()
        recipeIngredientRepository.deleteAll()
        recipeRepository.deleteAll()
        userRepository.deleteAll()
        ingredientRepository.deleteAll()
    }

}
