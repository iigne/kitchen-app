package com.kitchenapp.kitchenappapi.service

import com.kitchenapp.kitchenappapi.dto.request.RequestRecipeDTO
import com.kitchenapp.kitchenappapi.model.Recipe
import com.kitchenapp.kitchenappapi.providers.model.RecipeIngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.RecipeProvider
import com.kitchenapp.kitchenappapi.repository.RecipeRepository
import spock.lang.Specification

class RecipeServiceSpec extends Specification {

    RecipeRepository recipeRepository = Mock()

    IngredientService ingredientService = Mock()
    MeasurementService measurementService = Mock()
    UserService userService = Mock()

    RecipeService recipeService

    def setup() {
        recipeService = new RecipeService(recipeRepository, ingredientService, measurementService, userService)
    }

    //TODO :(
//    def "should create recipe"() {
//        given: "DTO is valid"
//        def ingredients = [RecipeIngredientProvider.make(), RecipeIngredientProvider.make()]
//        def savedValue = RecipeProvider.make(recipeIngredients: ingredients)
//        def dto = new RequestRecipeDTO()
//
//        when: "create is called"
//        then: "validations pass"
//        and: "database call is made"
//    }

//    def "should update recipe"() {
//        //TODO to test
//        // modify quantity
//        // add ingredient
//        // remove ingredient
//        given: "DTO is valid"
//
//        when: "update is called"
//
//        then: "validations pass"
//
//        and: "correct title and ingredient amounts are saved"
//        1 * recipeRepository.save(recipe -> {
//            recipe.recipeIngredients.size() == numberIngredients
//        }) >> new Recipe()
//
//        where:
//        newTitle    | numberIngredients | ingredient1 | ingredient2 | ingredient3
//        "modified"  | 2                 |  | |
//        "added"     | 3                 |
//        "removed"   | 1                 |
//    }

}
