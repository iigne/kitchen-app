package com.kitchenapp.kitchenappapi.service

import com.kitchenapp.kitchenappapi.dto.recipe.IngredientQuantityDTO
import com.kitchenapp.kitchenappapi.providers.CommonTestData
import com.kitchenapp.kitchenappapi.providers.model.*
import com.kitchenapp.kitchenappapi.repository.ShoppingListRepository
import spock.lang.Specification
import spock.lang.Unroll

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

    @Unroll
    def "should create shopping list item"() {
        given: "ingredients, measurements and user exist"
        def ingredient1 = IngredientProvider.make(id: id1)
        def ingredient2 = IngredientProvider.make(id: id2)
        def measurement1 = MeasurementProvider.make(id: CommonTestData.MEASUREMENT_ID_METRIC)
        def measurement2 = MeasurementProvider.make(id: CommonTestData.MEASUREMENT_ID, name: "jar", metricQuantity: 150)
        def user = UserProvider.make(id: CommonTestData.USER_ID)

        and: "one shopping list item exists"
        def shoppingIngredient1 = ShoppingUserIngredientProvider.make(user: user, ingredient: ingredient1,
                metricQuantity: quantity1, measurementId: CommonTestData.MEASUREMENT_ID_METRIC)

        when: "create is called for new shopping item"
        def shoppingIngredient2DTO = IngredientQuantityDTO.builder().ingredientId(id2).measurementId(measurement2id).quantity(quantity2).build()
        shoppingListService.createItemForUser(shoppingIngredient2DTO, user.id)

        then:
        1 * shoppingListRepository.findByUserIdAndIngredientId(user.id, id2) >> {
            id1 === id2 ? Optional.of(shoppingIngredient1) : Optional.empty()
        }
        1 * userService.findByIdOrThrow(CommonTestData.USER_ID) >> user
        1 * measurementService.findByIdOrThrow(measurement2id) >> {
            measurement2id === CommonTestData.MEASUREMENT_ID ? measurement2 : measurement1
        }
        1 * ingredientService.findByIdOrThrow(id2) >> ingredient2

        and: "shopping item with correct quantities is saved"
        1 * shoppingListRepository.save(savedIngredient -> {
            savedIngredient.ingredient.id == id2
            savedIngredient.metricQuantity == savedQuantity2
        })

        where:
        id1 | id2 | measurement1id                       | measurement2id                       | quantity1 | quantity2 | savedQuantity2
        1   | 1   | CommonTestData.MEASUREMENT_ID_METRIC | CommonTestData.MEASUREMENT_ID_METRIC | 150       | 150       | 300
        1   | 1   | CommonTestData.MEASUREMENT_ID_METRIC | CommonTestData.MEASUREMENT_ID        | 150       | 2         | 450
        1   | 2   | CommonTestData.MEASUREMENT_ID_METRIC | CommonTestData.MEASUREMENT_ID_METRIC | 150       | 150       | 150
    }

    def "should correctly tick and untick"() {
        given: "user and shopping item exists"
        def user = UserProvider.make()
        def shoppingItem = ShoppingUserIngredientProvider.make(user: user, ticked: tickBefore)

        when: "addOrRemoveTick is called"
        def result = shoppingListService.addOrRemoveTick(CommonTestData.INGREDIENT_ID, user.id)

        then: "correct tick value is saved"
        1 * shoppingListRepository.findByUserIdAndIngredientId(user.id, CommonTestData.INGREDIENT_ID) >> Optional.of(shoppingItem)
        1 * shoppingListRepository.save(ingredient -> {
            ingredient.ticked == tickAfter
        }) >> shoppingItem

        result == tickAfter

        where:
        tickBefore | tickAfter
        true       | false
        false      | true

    }
}
