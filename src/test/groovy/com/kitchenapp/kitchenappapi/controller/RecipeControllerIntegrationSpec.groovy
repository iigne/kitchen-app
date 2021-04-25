package com.kitchenapp.kitchenappapi.controller

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientQuantityDTO
import com.kitchenapp.kitchenappapi.dto.recipe.RequestRecipeDTO
import com.kitchenapp.kitchenappapi.providers.CommonTestData
import com.kitchenapp.kitchenappapi.providers.model.RecipeProvider
import spock.lang.Unroll

import static com.kitchenapp.kitchenappapi.controller.JsonParseHelper.*
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@WithMockCustomUser(id = MOCK_USER_ID)
class RecipeControllerIntegrationSpec extends AbstractIntegrationSpec {

    @Unroll
    def "should fetch recipes"() {
        given: "user exists in the database and is logged in"
        def user = getLoggedInUser()

        and: "ingredients and user ingredients exist"
        def ingredients = createIngredients()
        def testIngredient = ingredients.get(0)
        createUserIngredient(user, testIngredient)

        and: "there exist recipes in the database"
        createRecipe(user, ingredients)
        createRecipe(getAnotherUser(), ingredients)

        when: "user fetches recipes"
        def result = mvc.perform(get("/recipe/list/")
                .param("option", endpoint))
                .andReturn()

        then: "status is ok"
        result.response.status == 200

        and: "correct number of recipes are fetched in correct format"
        with(toRecipeDTOList(result.response.contentAsString)) { recipeList ->
            recipeList.size() == numberOfRecipes
            recipeList.each { recipeEntry ->
                recipeEntry.ingredients.size() == ingredients.size()
                recipeEntry.ingredients*.recipeQuantity == [150, 150, 150]
                recipeEntry.ingredients*.ownedQuantity.sort() == [0, 0, 150]
            }
        }

        where:
        endpoint  | numberOfRecipes
        "all"     | 2
        "liked"   | 1
        "created" | 1
    }

    def "should fail to create recipe"() {
        given: "recipeDTO has invalid values"
        def recipeDTO = new RequestRecipeDTO(title: "", method: "")

        when: "user attempts to save recipe"
        def result = mvc.perform(post("/recipe").with(csrf())
                .contentType("application/json")
                .content(toJson(recipeDTO)))
                .andReturn()

        then: "request fails"
        result.response.status == 400
        with(toApiError(result.response.contentAsString)) {
            errorMessage == "Some fields contain invalid values"
        }

    }

    def "should delete recipe"() {
        given: "user exists in the database and is logged in"
        def user = getLoggedInUser()
        and: "user has a recipe in the database"
        def recipe = recipeRepository.save(RecipeProvider.make(author: user, users: []))

        when: "user asks to delete recipe"
        def result = mvc.perform(delete("/recipe")
                .param("recipeId", recipe.getId() as String))
                .andReturn()

        then: "status is ok"
        result.response.status == 200

        and: "recipe is not in the database anymore"
        recipeRepository.findById(recipe.id) == Optional.empty()
    }

    def "should fail to delete recipe"() {
        given: "user exists in the database and is logged in"
        def user = getLoggedInUser()
        and: "there is a recipe in the database created by another user"
        def recipe = recipeRepository.save(RecipeProvider.make(author: getAnotherUser(), users: []))

        when: "user asks to delete recipe"
        def result = mvc.perform(delete("/recipe")
                .param("recipeId", recipe.getId() as String))
                .andReturn()

        then: "received status is 403 forbidden"
        result.response.status == 403

        and: "response gives an explanation"
        with(toApiError(result.response.contentAsString)) {
            errorMessage == "You are not allowed to perform this operation"
        }

        and: "recipe is still in the database"
        recipeRepository.findById(recipe.id).isPresent()
    }

    def "should update recipe"() {
        given: "user exists in the database and is logged in"
        def user = getLoggedInUser()
        and: "there exist ingredients and measurements"
        def savedIngredients = createIngredients()
        def recipeIngredient1 = savedIngredients.get(0)
        def recipeIngredient2 = savedIngredients.get(1)
        def addedIngredient = savedIngredients.get(2)
        def customMeasurement = createMeasurement()

        and: "user has a recipe in the database"
        def recipe = createRecipe(user, [recipeIngredient1, recipeIngredient2])

        and: "DTO is valid"
        def ingredientsDTO = [
                new IngredientQuantityDTO(ingredientId: recipeIngredient1.id, measurementId: customMeasurement.id, quantity: 3),
                new IngredientQuantityDTO(ingredientId: recipeIngredient2.id, measurementId: CommonTestData.MEASUREMENT_ID_METRIC, quantity: 500),
                new IngredientQuantityDTO(ingredientId: addedIngredient.id, measurementId: CommonTestData.MEASUREMENT_ID_METRIC, quantity: 100)
        ]
        def dto = new RequestRecipeDTO(id: recipe.id, title: newTitle, method: newMethod, ingredients: ingredientsDTO)

        when: "user asks to edit recipe"
        def result = mvc.perform(patch("/recipe").with(csrf())
                .contentType("application/json")
                .content(toJson(dto)))
                .andReturn()

        then: "status is ok"
        result.response.status == 200

        and: "recipe ingredients have been changed"
        with(toRecipeDTO(result.response.contentAsString)) {
            title == newTitle
            method == newMethod
            ingredients.size() == 3
            ingredients*.ingredientId.sort() == savedIngredients*.id.sort()
            ingredients*.recipeQuantity.sort() == [3, 100, 500]
            ingredients*.measurementId.sort() == [CommonTestData.MEASUREMENT_ID_METRIC, CommonTestData.MEASUREMENT_ID_METRIC, customMeasurement.id].sort()
        }

        where:
        newTitle        | newMethod
        "changed title" | "1. blah blah;"

    }

}
