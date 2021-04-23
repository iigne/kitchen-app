package com.kitchenapp.kitchenappapi.controller


import com.kitchenapp.kitchenappapi.model.*
import com.kitchenapp.kitchenappapi.providers.model.*
import com.kitchenapp.kitchenappapi.repository.IngredientRepository
import com.kitchenapp.kitchenappapi.repository.RecipeRepository
import com.kitchenapp.kitchenappapi.repository.ShoppingListRepository
import com.kitchenapp.kitchenappapi.repository.UserIngredientRepository
import com.kitchenapp.kitchenappapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

/**
 * Abstract class containing configuration and common methods for integration tests.
 * Also deals with data preparation and cleanup after tests.
 *
 * This ensures tests only have the code for testing rather all the
 * prep and cleanup.
 *
 * Note: the <code>@Sql</code> annotation resets numbering so that we always have
 * the same ID for the test user.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(statements = "ALTER TABLE [user] ALTER COLUMN id RESTART WITH 666")
abstract class AbstractIntegrationSpec extends Specification {

    static final int MOCK_USER_ID = 666
    static final int OTHER_MOCK_USER_ID = 667

    @Autowired
    protected MockMvc mvc

    @Autowired
    IngredientRepository ingredientRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    RecipeRepository recipeRepository

    @Autowired
    UserIngredientRepository userIngredientRepository

    @Autowired
    ShoppingListRepository shoppingListRepository

    def setup() {
        userRepository.deleteAll()
    }

    def cleanup() {
        userIngredientRepository.deleteAll()
        shoppingListRepository.deleteAll()
        recipeRepository.deleteAll()
        ingredientRepository.deleteAll()
    }

    protected User getLoggedInUser() {
        def auth = SecurityContextHolder.getContext().authentication
        return userRepository.findById(MOCK_USER_ID).orElse(
                userRepository.save(UserProvider.make(username: auth.getName())))
    }

    protected User getAnotherUser() {
        return userRepository.findById(OTHER_MOCK_USER_ID).orElse(
                userRepository.save(UserProvider.make(id: OTHER_MOCK_USER_ID, username: "differentName")))
    }

    protected List<Ingredient> createIngredients() {
        def ingredients = [IngredientProvider.make(name: "Ingredient1"), IngredientProvider.make(name: "Ingredient2"), IngredientProvider.make(name: "Ingredient3")]
        return ingredientRepository.saveAll(ingredients)
    }

    protected Recipe createRecipe(user, ingredients) {
        def recipe = recipeRepository.save(RecipeProvider.make(author: user, users: [user]))
        def recipeIngredients = []
        ingredients.each { it ->
            recipeIngredients.add(
                    RecipeIngredientProvider.make(recipe: recipe, ingredient: it, metricQuantity: 150))
        }
        recipe.setRecipeIngredients(recipeIngredients as Set<RecipeIngredient>)
        return recipeRepository.save(recipe)
    }

    protected UserIngredient creatUserIngredient(user, ingredient) {
        def userIngredient = UserIngredientProvider.make(user: user, ingredient: ingredient, metricQuantity: 150)
        return userIngredientRepository.save(userIngredient)
    }

    protected List<ShoppingUserIngredient> createShoppingListItems(user, ingredients) {
        return shoppingListRepository.saveAll([
                ShoppingUserIngredientProvider.make(user: user, ingredient: ingredients.get(0), ticked: true),
                ShoppingUserIngredientProvider.make(user: user, ingredient: ingredients.get(1)),
                ShoppingUserIngredientProvider.make(user: getAnotherUser(), ingredient: ingredients.get(2)),
        ])
    }

}
