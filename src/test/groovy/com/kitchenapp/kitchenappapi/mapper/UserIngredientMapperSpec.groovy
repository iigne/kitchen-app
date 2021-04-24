package com.kitchenapp.kitchenappapi.mapper


import com.kitchenapp.kitchenappapi.dto.useringredient.RequestUserIngredientDTO
import com.kitchenapp.kitchenappapi.mapper.useringredient.UserIngredientMapper
import com.kitchenapp.kitchenappapi.model.ingredient.MetricUnit
import com.kitchenapp.kitchenappapi.providers.CommonTestData
import com.kitchenapp.kitchenappapi.providers.model.IngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.MeasurementProvider
import com.kitchenapp.kitchenappapi.providers.model.UserIngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.UserProvider
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

class UserIngredientMapperSpec extends Specification {

    def "should correctly convert to entity"() {

        given: "valid ingredient and measurement"
        def metricMeasurement = MeasurementProvider.make()
        def measurement = MeasurementProvider.make(id: CommonTestData.MEASUREMENT_ID, name: "Jar", metricQuantity: 150, metricUnit: MetricUnit.GRAMS)
        def usedMeasurement = measurementId == CommonTestData.MEASUREMENT_ID ? measurement : metricMeasurement
        def ingredient = IngredientProvider.make(measurements: [metricMeasurement, measurement])

        and:
        def dto = new RequestUserIngredientDTO(measurementId: measurementId, quantity: inputQuantity,
                ingredientId: CommonTestData.INGREDIENT_ID, expiryDate: inputExpiry, dateBought: inputAdded
        )

        when:
        def entity = UserIngredientMapper.toEntity(dto, ingredient, UserProvider.make(), usedMeasurement)

        then:
        with(entity) {
            metricQuantity == expectedMetric
            dateAdded == expectedAdded
            dateExpiry == expectedExpiry
        }
        where:
        measurementId                        | inputQuantity | inputExpiry              | inputAdded                 || expectedMetric | expectedAdded              | expectedExpiry
        CommonTestData.MEASUREMENT_ID        | 2             | null                     | null                       || 2 * 150        | LocalDate.now()            | LocalDate.now().plusDays(14)
        CommonTestData.MEASUREMENT_ID_METRIC | 150           | LocalDate.of(2021, 5, 5) | LocalDate.of(2020, 12, 31) || 150            | LocalDate.of(2020, 12, 31) | LocalDate.of(2021, 5, 5)

    }

    @Unroll
    def "should correctly convert to DTO"() {
        given: "valid ingredient and measurement"
        def measurement = MeasurementProvider.make(id: CommonTestData.MEASUREMENT_ID, name: "Jar", metricQuantity: 150, metricUnit: MetricUnit.GRAMS)
        def ingredient = IngredientProvider.make(measurements: [MeasurementProvider.make(), measurement])

        and: "valid UserIngredient"
        def entity = UserIngredientProvider.make(ingredient: ingredient, measurement: measurement, metricQuantity: savedMetric)

        when: "convert to DTO"
        def dto = UserIngredientMapper.toDTO(entity)

        then:
        with(dto) {
            with(quantity) {
                measurementId == CommonTestData.MEASUREMENT_ID
                measurementName == measurement.name
                quantity == expectedCustom
            }
        }
        where:
        savedMetric || expectedCustom
        300         || 2
        75          || 0.5
        37.5        || 0.25
        50          || 0.33
    }

}
