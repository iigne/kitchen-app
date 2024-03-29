package com.kitchenapp.kitchenappapi.service

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientQuantityDTO
import com.kitchenapp.kitchenappapi.dto.recipe.RequestRecipeDTO
import com.kitchenapp.kitchenappapi.model.ingredient.Ingredient
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement
import com.kitchenapp.kitchenappapi.providers.CommonTestData
import com.kitchenapp.kitchenappapi.providers.model.*
import com.kitchenapp.kitchenappapi.repository.recipe.RecipeIngredientRepository
import com.kitchenapp.kitchenappapi.repository.recipe.RecipeRepository
import com.kitchenapp.kitchenappapi.service.ingredient.IngredientService
import com.kitchenapp.kitchenappapi.service.ingredient.MeasurementService
import com.kitchenapp.kitchenappapi.service.recipe.RecipeService
import com.kitchenapp.kitchenappapi.service.user.UserService
import spock.lang.Specification
import spock.lang.Unroll

class RecipeServiceSpec extends Specification {

    RecipeRepository recipeRepository = Mock()
    RecipeIngredientRepository recipeIngredientRepository = Mock()

    UserService userService = Mock()
    MeasurementService measurementService = Spy()
    IngredientService ingredientService = Mock()

    RecipeService recipeService

    def setup() {
        recipeService = new RecipeService(recipeRepository, recipeIngredientRepository, ingredientService, measurementService, userService)
    }

    def "should create recipe"() {
        given: "user and ingredients exist"
        def user = UserProvider.make()
        def ingredient1 = IngredientProvider.make(id: id1, name: "Pasta")
        def ingredient2 = IngredientProvider.make(id: id2, name: "Pesto")

        def measurement1 = MeasurementProvider.make(id: measurement1id)
        def measurement2 = MeasurementProvider.make(id: measurement2id)

        def createdRecipe = RecipeProvider.make(title: title, method: method, author: user)

        Map<Integer, Measurement> measurementMap = Map.of(CommonTestData.MEASUREMENT_ID_METRIC, MeasurementProvider.make())
        Map<Integer, Ingredient> ingredientMap = Map.of(id1, ingredient1, id2, ingredient2)

        and: "DTO is valid"
        def ingredients = [
                new IngredientQuantityDTO(ingredientId: id1, measurementId: measurement1id, quantity: quantity1),
                new IngredientQuantityDTO(ingredientId: id2, measurementId: measurement2id, quantity: quantity2)
        ]
        def dto = new RequestRecipeDTO(title: title, method: method, ingredients: ingredients)

        when: "create is called"
        recipeService.create(dto, user.id)

        then: "validations pass"
        userService.findByIdOrThrow(user.id) >> user

        and: "recipe is saved without ingredients"
        1 * recipeRepository.save(savedRecipe -> {
            savedRecipe.id == 0
            savedRecipe.title == title
            savedRecipe.method == method
            savedRecipe.author == user
            savedRecipe.recipeIngredients == null
        }) >> createdRecipe

        and: "recipe ingredients are fetched"
        ingredientService.extractIngredientsFromDTOs(ingredients) >> {ingredientMap}
        measurementService.extractMeasurementsFromDTOs(ingredients) >> {measurementMap}
        1 * measurementService.getFromMapOrThrow(measurement1id, measurementMap)
        1 * measurementService.getFromMapOrThrow(measurement2id, measurementMap)

        and: "recipe saved with ingredients"
        1 * recipeRepository.save(recipe -> {
            recipe.id == createdRecipe.id
            recipe.title == createdRecipe.title
            recipe.method == createdRecipe.method
            recipe.author == createdRecipe.author
            recipe.recipeIngredients*.ingredient.id.sort() == [id1, id2].sort()
            recipe.recipeIngredients*.metricQuantity.sort() == [quantity1, quantity2].sort()

        }) >> createdRecipe

        and: "ingredient quantities for saved recipe are fetched"
        1 * recipeIngredientRepository.fetchIngredientQuantitiesByUserIdAndRecipeId(user.id, createdRecipe.id)

        where:
        title         | method                      | id1 | id2 | measurement1id                       | measurement2id                       | quantity1 | quantity2
        "Pesto pasta" | "1.Cook pasta;2.Add pesto;" | 1   | 2   | CommonTestData.MEASUREMENT_ID_METRIC | CommonTestData.MEASUREMENT_ID_METRIC | 200       | 25
    }

    @Unroll
    def "should handle liking/unliking recipe"() {
        given: "recipe and user exists"
        def user = UserProvider.make()
        def recipe = RecipeProvider.make(id: CommonTestData.RECIPE_ID, author: user, users: usersList)

        when: "recipe is liked/unliked"
        def result = recipeService.removeOrAddFromUserRecipes(recipe.id, user.id)

        then: "user and recipe is fetched"
        1 * userService.findByIdOrThrow(user.id) >> user
        1 * recipeRepository.findById(recipe.id) >> Optional.of(recipe)

        and: "correct user is added/removed correctly"
        1 * recipeRepository.save(savedRecipe -> {
            savedRecipe.users*.id.sort() == changedUsersList*.id.sort()
        }) >> recipe

        and: "correct liked status is returned"
        result == liked

        where:
        usersList                                         || changedUsersList                                  | liked
        []                                                || [UserProvider.make()]                             | true
        [UserProvider.make()]                             || []                                                | false
        [UserProvider.make(id: 123), UserProvider.make()] || [UserProvider.make(id: 123)]                      | false
        [UserProvider.make(id: 123)]                      || [UserProvider.make(id: 123), UserProvider.make()] | true
    }

    @Unroll
    def "should update recipe"() {

        given: "user, measurements and ingredients exist"
        def user = UserProvider.make()
        def ingredient1 = IngredientProvider.make(id: id1, name: "Pasta")
        def ingredient2 = IngredientProvider.make(id: id2, name: "Pesto")

        Map<Integer, Measurement> measurementMap = Map.of(CommonTestData.MEASUREMENT_ID_METRIC, MeasurementProvider.make())
        Map<Integer, Ingredient> ingredientMap = Map.of(id1, ingredient1, id2, ingredient2)

        and: "recipe with ingredients exists"
        def originalRecipe = RecipeProvider.make(author: user)
        def recipeIngredient1 = RecipeIngredientProvider.make(ingredient: ingredient1, recipe: originalRecipe, metricQuantity: 150)
        def recipeIngredient2 = RecipeIngredientProvider.make(ingredient: ingredient2, recipe: originalRecipe, metricQuantity: 150)

        originalRecipe.recipeIngredients = numIngredientsBefore == 2 ? [recipeIngredient1, recipeIngredient2] : [recipeIngredient1]

        and: "DTO is valid"
        def newIngredient1 = new IngredientQuantityDTO(ingredientId: id1, quantity: quantity1, measurementId: CommonTestData.MEASUREMENT_ID_METRIC)
        def newIngredient2 = new IngredientQuantityDTO(ingredientId: id2, quantity: quantity2, measurementId: CommonTestData.MEASUREMENT_ID_METRIC)
        def newIngredients = id2 != 0 ? [newIngredient1, newIngredient2] : [newIngredient1]

        def dto = new RequestRecipeDTO(id: CommonTestData.RECIPE_ID, title: newTitle, ingredients: newIngredients)

        when: "update is called"
        recipeService.update(dto, user.id)

        then: "correct repositories and services are called"

        1 * recipeRepository.findById(CommonTestData.RECIPE_ID) >> Optional.of(originalRecipe)
        ingredientService.extractIngredientsFromDTOs(newIngredients) >> {ingredientMap}
        measurementService.extractMeasurementsFromDTOs(newIngredients) >> {measurementMap}

        _ * measurementService.getFromMapOrThrow(_, measurementMap)

        and: "correct title and ingredient amounts are saved"
        1 * recipeRepository.save(updatedRecipe -> {
            updatedRecipe.title == newTitle
            updatedRecipe.recipeIngredients.size() == numIngredientsAfter
            updatedRecipe.recipeIngredients*.metricQuantity.sort() == [quantity1, quantity2].subList(0, numIngredientsAfter).sort()
        }) >> originalRecipe

        and: "new ingredient quantities for saved recipe are fetched"
        1 * recipeIngredientRepository.fetchIngredientQuantitiesByUserIdAndRecipeId(user.id, CommonTestData.RECIPE_ID)

        where:
        newTitle           | numIngredientsBefore | id1 | id2 | quantity1 | quantity2 || numIngredientsAfter
        "modified"         | 2                    | 1   | 2   | 150       | 300       || 2
        "addedAndModified" | 1                    | 1   | 2   | 500       | 150       || 2
        "removed"          | 2                    | 1   | 0   | 150       | 0         || 1
    }

}
