package com.kitchenapp.kitchenappapi.controller

import com.kitchenapp.kitchenappapi.dto.request.RequestRecipeDTO
import com.kitchenapp.kitchenappapi.providers.model.RecipeProvider
import com.kitchenapp.kitchenappapi.repository.RecipeRepository
import org.springframework.beans.factory.annotation.Autowired

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WithMockCustomUser(id = MOCK_USER_ID)
class RecipeControllerIntegrationSpec extends AbstractIntegrationSpec {

    @Autowired
    RecipeRepository recipeRepository

    def cleanup() {
        recipeRepository.deleteAll()
    }

    def "should fetch recipes"() {
//        given: "user exists in the database and is logged in"
//        def user = getUser()
//        and: "there exist recipes in the database"
//
//
//        where:
//        endpoint | number
//        "all"    | 3
//        "liked"  | 2
//        "created"| 1
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

}
