package com.kitchenapp.kitchenappapi.service

import com.kitchenapp.kitchenappapi.dto.QuantityDTO
import com.kitchenapp.kitchenappapi.model.Ingredient
import com.kitchenapp.kitchenappapi.model.Measurement
import com.kitchenapp.kitchenappapi.model.MetricUnit
import com.kitchenapp.kitchenappapi.model.User
import com.kitchenapp.kitchenappapi.model.UserIngredient
import com.kitchenapp.kitchenappapi.providers.CommonTestData
import com.kitchenapp.kitchenappapi.providers.dto.UserIngredientDTOProvider
import com.kitchenapp.kitchenappapi.providers.model.IngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.MeasurementProvider
import com.kitchenapp.kitchenappapi.providers.model.UserIngredientProvider
import com.kitchenapp.kitchenappapi.repository.IngredientRepository
import com.kitchenapp.kitchenappapi.repository.MeasurementRepository
import com.kitchenapp.kitchenappapi.repository.UserIngredientRepository
import com.kitchenapp.kitchenappapi.repository.UserRepository
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityNotFoundException

class UserIngredientServiceSpec extends Specification {

    UserIngredientRepository userIngredientRepository = Mock()
    UserRepository userRepository = Mock()
    IngredientRepository ingredientRepository = Mock()
    MeasurementService measurementService = Mock()

    UserIngredientService userIngredientService

    def setup() {
        userIngredientService = new UserIngredientService(userIngredientRepository, userRepository,
                ingredientRepository, measurementService)
    }

    def "should create ingredient"() {
        given: "DTO is valid"
        def quantityDTO = new QuantityDTO(measurementId: CommonTestData.MEASUREMENT_ID_METRIC, quantity: inputQuantity)
        def dto = UserIngredientDTOProvider.make(quantities: [quantityDTO])
        def measurement = MeasurementProvider.make()

        when: "create is called"
        userIngredientService.create(CommonTestData.USER_ID, dto)

        then: "validations pass"
        1 * userIngredientRepository.findByUserIdAndIngredientId(CommonTestData.USER_ID, CommonTestData.INGREDIENT_ID) >> Optional.empty()
        1 * userRepository.findById(CommonTestData.USER_ID) >> Optional.of(new User(id: CommonTestData.USER_ID))
        1 * ingredientRepository.findById(CommonTestData.INGREDIENT_ID) >> Optional.of(IngredientProvider.make(measurement: [measurement]))
        1 * measurementService.findByIdOrThrow(measurement.id) >> measurement

        and: "ingredient created in database"
        1 * userIngredientRepository.save(_) >> UserIngredientProvider.make()

        where:
        inputQuantity << 10
    }

    @Unroll
    def "should fail to create ingredient when there's data conflicts"() {
        given: "DTO is valid"
        def quantityDTO = new QuantityDTO(measurementId: CommonTestData.MEASUREMENT_ID_METRIC, quantity: 10)
        def dto = UserIngredientDTOProvider.make(quantity: quantityDTO)

        when: "create is called"
        userIngredientService.create(CommonTestData.USER_ID, dto)

        then: "validation is performed"
        userIngredientRepository.findByUserIdAndIngredientId(CommonTestData.USER_ID, CommonTestData.INGREDIENT_ID) >> userIngredientValue
        userRepository.findById(CommonTestData.USER_ID) >> userValue
        ingredientRepository.findById(CommonTestData.INGREDIENT_ID) >> ingredientValue

        and: "exception is thrown"
        def ex = thrown(exceptionClass)
        ex.message == message

        and: "no database interactions performed"
        0 * userIngredientRepository.save(_)

        where:
        userIngredientValue                        | userValue               | ingredientValue  || exceptionClass          | message
        Optional.of(UserIngredientProvider.make()) | Optional.empty()        | Optional.empty() || IllegalStateException   | "userId $CommonTestData.USER_ID and ingredientId $CommonTestData.INGREDIENT_ID already exists"
        Optional.empty()                           | Optional.empty()        | Optional.empty() || EntityNotFoundException | "userId $CommonTestData.USER_ID not found"
        Optional.empty()                           | Optional.of(new User()) | Optional.empty() || EntityNotFoundException | "ingredientId $CommonTestData.INGREDIENT_ID not found"
    }

    @Unroll
    def "should update quantity"() {
        given: "quantity is valid"
        def quantityDTO = new QuantityDTO(measurementId: inputMeasurementId, quantity: inputQuantity)

        when: "update is called"
        userIngredientService.updateQuantity(CommonTestData.USER_ID, CommonTestData.INGREDIENT_ID, quantityDTO)

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
}
