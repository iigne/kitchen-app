package com.kitchenapp.kitchenappapi.service

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientQuantityDTO
import com.kitchenapp.kitchenappapi.dto.useringredient.RequestUserIngredientDTO
import com.kitchenapp.kitchenappapi.model.ingredient.Measurement
import com.kitchenapp.kitchenappapi.model.ingredient.MetricUnit
import com.kitchenapp.kitchenappapi.model.useringredient.UserIngredientId
import com.kitchenapp.kitchenappapi.providers.CommonTestData

import com.kitchenapp.kitchenappapi.providers.model.IngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.MeasurementProvider
import com.kitchenapp.kitchenappapi.providers.model.UserIngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.UserProvider
import com.kitchenapp.kitchenappapi.repository.useringredient.UserIngredientRepository
import com.kitchenapp.kitchenappapi.service.ingredient.IngredientService
import com.kitchenapp.kitchenappapi.service.ingredient.MeasurementService
import com.kitchenapp.kitchenappapi.service.user.UserService
import com.kitchenapp.kitchenappapi.service.useringredient.UserIngredientService
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityNotFoundException

class UserIngredientServiceSpec extends Specification {

    UserIngredientRepository userIngredientRepository = Mock()
    UserService userService = Mock()
    IngredientService ingredientService = Mock()
    MeasurementService measurementService = Spy()

    UserIngredientService userIngredientService

    def setup() {
        userIngredientService = new UserIngredientService(userIngredientRepository, measurementService,
                userService, ingredientService)
    }

    def "should create ingredient"() {
        given: "DTO is valid"
        def dto = new RequestUserIngredientDTO(measurementId: CommonTestData.MEASUREMENT_ID_METRIC, quantity: inputQuantity, ingredientId: CommonTestData.INGREDIENT_ID)
        def measurement = MeasurementProvider.make()

        when: "create is called"
        userIngredientService.create(CommonTestData.USER_ID, dto)

        then: "validations pass"
        1 * userIngredientRepository.findByUserIdAndIngredientId(CommonTestData.USER_ID, CommonTestData.INGREDIENT_ID) >> Optional.empty()
        1 * userService.findByIdOrThrow(CommonTestData.USER_ID) >> UserProvider.make()
        1 * ingredientService.findByIdOrThrow(CommonTestData.INGREDIENT_ID) >> IngredientProvider.make(measurement: [measurement])
        1 * measurementService.findByIdOrThrow(measurement.id) >> measurement

        and: "ingredient created in database"
        1 * userIngredientRepository.save(_) >> UserIngredientProvider.make()

        where:
        inputQuantity << 10
    }

    def "should fail to create ingredient when there's data conflicts"() {
        given: "DTO is valid"
        def dto = new RequestUserIngredientDTO(measurementId: CommonTestData.MEASUREMENT_ID_METRIC, quantity: 150, ingredientId: CommonTestData.INGREDIENT_ID)

        when: "create is called"
        userIngredientService.create(CommonTestData.USER_ID, dto)

        then: "exceptions are thrown"
        userService.findByIdOrThrow(CommonTestData.USER_ID) >> { throw new EntityNotFoundException() }
        ingredientService.findByIdOrThrow(CommonTestData.INGREDIENT_ID) >> { throw new EntityNotFoundException() }

        thrown(EntityNotFoundException.class)

        and: "no database interactions performed"
        0 * userIngredientRepository.save(_)
    }

    @Unroll
    def "should update quantity"() {
        given: "quantity is valid"
        def quantityDTO = new RequestUserIngredientDTO(ingredientId: CommonTestData.INGREDIENT_ID, measurementId: inputMeasurementId, quantity: inputQuantity)

        when: "update is called"
        userIngredientService.update(CommonTestData.USER_ID, quantityDTO)

        then: "validations pass"
        1 * userIngredientRepository.findByUserIdAndIngredientId(CommonTestData.USER_ID, CommonTestData.INGREDIENT_ID) >> Optional.of(UserIngredientProvider.make())
        _ * measurementService.findByIdOrThrow(CommonTestData.MEASUREMENT_ID_METRIC) >> MeasurementProvider.make()
        _ * measurementService.findByIdOrThrow(CommonTestData.MEASUREMENT_ID) >> MeasurementProvider.make(id: CommonTestData.MEASUREMENT_ID, name: "Jar", metricQuantity: 150, metricUnit: MetricUnit.GRAMS)

        and: "user ingredient is saved with correct quantity"
        1 * userIngredientRepository.save(ingredient -> {
            ingredient.metricQuantity == outputMetric
        }) >> UserIngredientProvider.make()

        where:
        inputMeasurementId                   | inputQuantity || outputMetric
        CommonTestData.MEASUREMENT_ID_METRIC | 300           || 300
        CommonTestData.MEASUREMENT_ID        | 2             || 300
        CommonTestData.MEASUREMENT_ID        | 1.5           || 225
    }

    @Unroll
    def "should update quantities"() {
        given: "ingredients and measurements exist"
        def ingredient1 = IngredientProvider.make(id: id1)
        def ingredient2 = IngredientProvider.make(id: id2)

        def measurement1 = MeasurementProvider.make(id: CommonTestData.MEASUREMENT_ID_METRIC)
        def measurement2 = MeasurementProvider.make(id: CommonTestData.MEASUREMENT_ID, name: "jar", metricQuantity: 150)
        Map<Integer, Measurement> measurementsMap = measurement1id == measurement2id ? Map.of(measurement1.id, measurement1) : Map.of(measurement1.id, measurement1, measurement2id, measurement2)

        and: "user and user ingredients exist"
        def user = UserProvider.make()

        def userIngredient1 = Spy(UserIngredientProvider.make(user: user, ingredient: ingredient1, metricQuantity: 150), {
            getId() >> new UserIngredientId(ingredientId: id1, userId: user.id)
        })
        def userIngredient2 = Spy(UserIngredientProvider.make(user: user, ingredient: ingredient2, metricQuantity: 150), {
            getId() >> new UserIngredientId(ingredientId: id2, userId: user.id)
        })

        and: "quantities are valid"
        def quantityDTO1 = new IngredientQuantityDTO(ingredientId: id1, measurementId: measurement1id, quantity: quantity1)
        def quantityDTO2 = new IngredientQuantityDTO(ingredientId: id2, measurementId: measurement2id, quantity: quantity2)

        when: "update quantities is called"
        userIngredientService.updateQuantities(user.id, [quantityDTO1, quantityDTO2])

        then: "measurements are found"
        1 * measurementService.extractMeasurementsFromDTOs([quantityDTO1, quantityDTO2]) >> measurementsMap
        2 * measurementService.getFromMapOrThrow(_, measurementsMap)

        and: "user ingredients are fetched"
        1 * userIngredientRepository.findAllByUserIdAndIngredientIdIn(user.id, [id1, id2]) >> [userIngredient1, userIngredient2]

        and: "correct quantities are saved"
        1 * userIngredientRepository.saveAll(userIngredients -> {
            userIngredients*.id.ingredientId.sort() == [id1, id2].sort()
            userIngredients*.metricQuantity.sort() == [updatedQuantity1, updatedQuantity2].sort()
        })

        where:
        id1 | id2 | measurement1id                       | measurement2id                       | quantity1 | quantity2 || updatedQuantity1 | updatedQuantity2
        1   | 2   | CommonTestData.MEASUREMENT_ID_METRIC | CommonTestData.MEASUREMENT_ID_METRIC | 50        | 100       || 100              | 50
        1   | 2   | CommonTestData.MEASUREMENT_ID_METRIC | CommonTestData.MEASUREMENT_ID        | 50        | 1         || 100              | 0
        1   | 2   | CommonTestData.MEASUREMENT_ID_METRIC | CommonTestData.MEASUREMENT_ID_METRIC | 500       | 500       || 0                | 0
    }
}
