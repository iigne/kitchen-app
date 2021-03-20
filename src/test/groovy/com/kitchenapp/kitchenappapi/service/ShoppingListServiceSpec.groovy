package com.kitchenapp.kitchenappapi.service


import com.kitchenapp.kitchenappapi.providers.model.IngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.ShoppingUserIngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.UserIngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.UserProvider
import com.kitchenapp.kitchenappapi.repository.ShoppingListRepository
import spock.lang.Specification

class ShoppingListServiceSpec extends Specification {

    ShoppingListRepository shoppingListRepository = Mock()
    MeasurementService measurementService = Mock()
    UserService userService = Mock()
    IngredientService ingredientService = Mock()
    UserIngredientService userIngredientService = Mock()

    ShoppingListService shoppingListService

    def setup() {
        shoppingListService = new ShoppingListService(shoppingListRepository, measurementService, userService,
                ingredientService, userIngredientService)
    }

    def "should clear ticked items and import to user stock"() {
        given: "ingredients and user exist"
        def ingredient1 = IngredientProvider.make(id: id1)
        def ingredient2 = IngredientProvider.make(id: id2)
        def user = UserProvider.make()

        "shopping list items exist"
        def shoppingIngredient1 = ShoppingUserIngredientProvider.make(user: user, ingredient: ingredient1, ticked: true)
        def shoppingIngredient2 = ShoppingUserIngredientProvider.make(user: user, ingredient: ingredient2, ticked: true)

        and: "user ingredients exist"
        def userIngredient1 = UserIngredientProvider.make(ingredient: ingredient1, user: user)

        when: "clear and import is called"
        shoppingListService.clearAndImport(user.id)

        then: "ticked shopping list items are collected"
        1 * shoppingListRepository.findAllByUserIdAndTickedIsTrue(user.id) >> [shoppingIngredient1, shoppingIngredient2]

        and: "ticked items are imported to user stock"
        1 * userIngredientService.getByIds([id1, id2], user.id) >> [userIngredient1]
        1 * userIngredientService.saveAll(savedIngredients -> {
            savedIngredients.size == 2
            savedIngredients*.ingredient.id.sort() == [id1, id2].sort()
            savedIngredients*.metricQuantity.sort() == [userQuantity1 + shoppingQuantity1, shoppingQuantity2].sort()
        })

        and: "ticked items are removed from shopping list"
        1 * shoppingListRepository.deleteAll(deletedItems -> {
            deletedItems.size == 2
            deletedItems*.ingredient.id.sort() == [id1, id2].sort()
        })

        where:
        id1 | id2 | userQuantity1 | shoppingQuantity1 | shoppingQuantity2
        10  | 11  | 100           | 100               | 100
    }
}
