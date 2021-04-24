package com.kitchenapp.kitchenappapi.controller

import com.kitchenapp.kitchenappapi.dto.recipe.RequestUserIngredientDTO
import com.kitchenapp.kitchenappapi.providers.CommonTestData

import static com.kitchenapp.kitchenappapi.controller.JsonParseHelper.toJson
import static com.kitchenapp.kitchenappapi.controller.JsonParseHelper.toUserIngredientDTO
import static com.kitchenapp.kitchenappapi.controller.JsonParseHelper.toUserIngredientDTOList
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WithMockCustomUser(id = MOCK_USER_ID)
class UserIngredientControllerIntegrationSpec extends AbstractIntegrationSpec {

    def "should display user ingredients"() {
        given: "logged in user exists in database"
        def user = getLoggedInUser()

        and: "ingredients exist in db"
        def savedIngredients = createIngredients()

        and: "user ingredients exist in the db"
        createUserIngredient(user, savedIngredients.get(0))

        when: "request is made to display all user ingredients by user"
        def result = mvc.perform(get("/user-ingredient"))
                .andReturn()
        then: "status is ok"
        result.response.status == 200

        and: "response body contains user ingredients"
        with(toUserIngredientDTOList(result.response.contentAsString)) { response ->
            response.size() == 1
        }
    }

    def "should delete user ingredient"() {
        given: "logged in user exists in database"
        def user = getLoggedInUser()

        and: "ingredients exist in db"
        def savedIngredients = createIngredients()

        and: "user ingredients exist in the db"
        def ingredient = savedIngredients.get(0)
        createUserIngredient(user, ingredient)

        when: "a request is made to delete user ingredient"
        def result = mvc.perform(delete("/user-ingredient")
                .param("ingredientId", ingredient.getId() as String)
        ).andReturn()

        then: "status is no content"
        result.response.status == 204

        and: "user has no ingredients"
        userIngredientRepository.findAllByUserId(user.id).size() == 0
    }

    def "should create user ingredient"() {
        given: "logged in user exists in database"
        def user = getLoggedInUser()

        and: "ingredients exist in db"
        def savedIngredients = createIngredients()
        def ingredient = savedIngredients.get(0)

        and: "DTO is valid"
        def dto = new RequestUserIngredientDTO(ingredientId: ingredient.getId(), measurementId: CommonTestData.MEASUREMENT_ID_METRIC, quantity: inputQuantity)

        when: "request is made to create user ingredient"
        def result = mvc.perform(post("/user-ingredient")
                .with(csrf())
                .contentType("application/json")
                .content(toJson(dto)))
                .andReturn()

        then: "status is created"
        result.response.status == 201

        and: "user ingredient is returned"
        with(toUserIngredientDTO(result.response.contentAsString)) {userIngredient ->
            userIngredient.ingredient.id == ingredient.id
            userIngredient.quantity.quantity == inputQuantity
        }

        and: "user ingredient is in the database"
        userIngredientRepository.findAllByUserId(user.id).size() == 1

        where:
        inputQuantity << 150
    }

    def "should update user ingredient"() {
        given: "logged in user exists in database"
        def user = getLoggedInUser()

        and: "ingredients exist in db"
        def savedIngredients = createIngredients()
        def ingredient = savedIngredients.get(0)
        createUserIngredient(user, ingredient)

        and: "DTO is valid"
        def dto = new RequestUserIngredientDTO(ingredientId: ingredient.getId(), measurementId: CommonTestData.MEASUREMENT_ID_METRIC, quantity: inputQuantity)

        when: "request is made to update user ingredient"
        def result = mvc.perform(patch("/user-ingredient")
                .with(csrf())
                .contentType("application/json")
                .content(toJson(dto)))
                .andReturn()

        then: "status is ok"
        result.response.status == 200

        and: "updated user ingredient is returned"
        with(toUserIngredientDTO(result.response.contentAsString)) {userIngredient ->
            userIngredient.ingredient.id == ingredient.id
            userIngredient.quantity.quantity == inputQuantity
        }

        and: "only one user ingredient is in the database with updated quantity"
        def userIngredients = userIngredientRepository.findAllByUserId(user.id)
        userIngredients.size() == 1
        userIngredients.get(0).metricQuantity == inputQuantity

        where:
        inputQuantity << 500
    }

}
