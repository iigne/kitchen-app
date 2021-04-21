package com.kitchenapp.kitchenappapi.controller

import com.kitchenapp.kitchenappapi.providers.model.UserIngredientProvider
import com.kitchenapp.kitchenappapi.repository.UserIngredientRepository
import org.springframework.beans.factory.annotation.Autowired

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@WithMockCustomUser(id = MOCK_USER_ID)
class UserIngredientControllerIntegrationSpec extends AbstractIntegrationSpec {

    @Autowired
    private UserIngredientRepository userIngredientRepository

    def cleanup() {
        userIngredientRepository.deleteAll()
        ingredientRepository.deleteAll()
    }

    def "should display user ingredients"() {
        given: "logged in user exists in database"
        def user = getLoggedInUser()

        and: "ingredients exist in db"
        def savedIngredients = createIngredients()

        and: "user ingredients exist in the db"
        def userIngredients = [UserIngredientProvider.make(user: user, ingredient: savedIngredients.get(0), metricQuantity: 150)]
        userIngredientRepository.saveAll(userIngredients)

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
        def userIngredient = UserIngredientProvider.make(user: user, ingredient: ingredient, metricQuantity: 150)
        userIngredientRepository.save(userIngredient)

        when: "a request is made to delete user ingredient"
        def result = mvc.perform(delete("/user-ingredient")
                .param("ingredientId", ingredient.getId() as String)
        ).andReturn()

        then: "status is ok"
        result.response.status == 200

        and: "user has no ingredients"
        userIngredientRepository.findAllByUserId(user.id).size() == 0
    }

}
