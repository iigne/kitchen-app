package com.kitchenapp.kitchenappapi.controller

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientQuantityDTO
import com.kitchenapp.kitchenappapi.providers.CommonTestData

import static com.kitchenapp.kitchenappapi.controller.JsonParseHelper.toJson
import static com.kitchenapp.kitchenappapi.controller.JsonParseHelper.toShoppingItemDTOList
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@WithMockCustomUser(id = MOCK_USER_ID)
class ShoppingListControllerIntegrationSpec extends AbstractIntegrationSpec {

    def "should list all items for user"() {
        given: "logged in user exists in database"
        def user = getLoggedInUser()

        and: "there exist shopping list items in the database"
        def ingredients = createIngredients()
        createShoppingListItems(user, ingredients)

        when: "user asks to list all their items"
        def result = mvc.perform(get("/shopping"))
                .andReturn()

        then: "status is ok"
        result.response.status == 200

        and: "all shopping list items for user are retrieved"
        with(toShoppingItemDTOList(result.response.getContentAsString())) { items ->
            items.size() == 2
            items*.ingredient.id.sort() == [ingredients.get(0).id, ingredients.get(1).id].sort()
            items*.ticked.sort() == [false, true].sort()
        }
    }

    def "should clear list"() {
        given: "logged in user exists in database"
        def user = getLoggedInUser()

        and: "there exist shopping list items in the database"
        createShoppingListItems(user, createIngredients())

        when: "user asks to clear the shopping list"
        def result = mvc.perform(delete("/shopping/multiple"))
                .andReturn()

        then: "status is no content"
        result.response.status == 204

        and: "logged in user's shopping items have been deleted"
        shoppingListRepository.findAllByUserId(user.getId()).isEmpty()

        and: "other user's items are unaffected"
        !shoppingListRepository.findAll().isEmpty()
    }

    def "should import ticked items"() {
        given: "logged in user exists in database"
        def user = getLoggedInUser()

        and: "there exist shopping list items in the database"
        def ingredients = createIngredients()
        createShoppingListItems(user, ingredients)

        when: "user asks to clear the shopping list"
        def result = mvc.perform(post("/shopping/clear-and-import")
                .with(csrf()))
                .andReturn()

        then: "status is no content"
        result.response.status == 204

        and: "ticked items have been removed from user's list"
        def updatedList = shoppingListRepository.findAllByUserId(user.id)
        updatedList.size() == 1
        updatedList*.ticked == [false]

        and: "ticked items are in user ingredients"
        def userIngredients = userIngredientRepository.findAllByUserId(user.id)
        userIngredients.size() == 1
        userIngredients*.metricQuantity == [100]
    }

    def "should create items"() {
        given: "logged in user exists in database"
        def user = getLoggedInUser()

        and: "there exist ingredients in the database"
        def ingredients = createIngredients()

        when: "request is made to create shopping list items"
        def dtos = []
        ingredients.each {
            dtos.add(new IngredientQuantityDTO(
                    ingredientId: it.id, measurementId: CommonTestData.MEASUREMENT_ID_METRIC, quantity: 150))
        }

        def result = mvc.perform(post("/shopping/multiple")
                .with(csrf())
                .contentType("application/json")
                .content(toJson(dtos)))
        .andReturn()

        then: "status is created"
        result.response.status == 201

        and: "created shopping ingredients are shown"
        with(toShoppingItemDTOList(result.response.contentAsString)) { savedItems ->
            savedItems.size() == 3
            savedItems*.ingredient.id.sort() == ingredients*.id.sort()
            savedItems*.quantity.quantity.sort() == [150,150,150]
        }

        and: "user has ingredients in the database"
        shoppingListRepository.findAllByUserId(user.id).size() == 3
    }

}
