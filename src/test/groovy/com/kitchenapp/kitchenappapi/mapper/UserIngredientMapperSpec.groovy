package com.kitchenapp.kitchenappapi.mapper

import com.kitchenapp.kitchenappapi.dto.QuantityDTO
import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO
import com.kitchenapp.kitchenappapi.model.Measurement
import com.kitchenapp.kitchenappapi.model.MetricUnit
import com.kitchenapp.kitchenappapi.model.UserIngredient
import com.kitchenapp.kitchenappapi.providers.model.IngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.MeasurementProvider
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

class UserIngredientMapperSpec extends Specification {

    def "should correctly convert to entity"() {

        given: "valid ingredient and measurement"

        def measurement = useMeasurement ? MeasurementProvider.make() : null
        def ingredient = IngredientProvider.make(measurements: [measurement])
        and:
        def metric = useMeasurement ? null : new QuantityDTO(quantity: inputQuantity)
        def quantity = useMeasurement ? new QuantityDTO(measurementId: measurement.id, quantity: inputQuantity) : null
        def dto = new UserIngredientDTO(ingredientId: ingredient.id, quantity: quantity, metricQuantity: metric,
                expiryDate: inputExpiry, dateBought: inputAdded)
        when:
        def entity = UserIngredientMapper.toEntity(dto, ingredient, null, measurement)
        then:
        with(entity) {
            metricQuantity == expectedMetric
            dateAdded == expectedAdded
            dateExpiry == expectedExpiry
        }
        where:
        useMeasurement | inputQuantity | inputExpiry              | inputAdded                 || expectedMetric | expectedAdded            | expectedExpiry
        true           | 2             | null                     | null                       || 2 * 150        | LocalDate.now()          | LocalDate.now().plusDays(14)
        false          | 150           | LocalDate.of(2021, 5, 5) | LocalDate.of(2020, 12, 31) || 150            | LocalDate.of(2020, 12, 31) | LocalDate.of(2021, 5, 5)

    }

    @Unroll
    def "should correctly convert to DTO"() {
        given: "valid ingredient and measurement"
        def measurement = useMeasurement ? new Measurement(id: 1, name: "Jar", metricQuantity: 150, metricUnit: MetricUnit.GRAMS) : null
        def ingredient = IngredientProvider.make(measurements: [measurement])
        and: "valid UserIngredient"
        def entity = new UserIngredient(ingredient: ingredient, customMeasurement: measurement, metricQuantity: savedMetric)

        when: "convert to DTO"
        def dto = UserIngredientMapper.toDTO(entity)
        then: "should be ok"
        with(dto) {
            metricQuantity.quantity == savedMetric
            metricQuantity.measurementName == MetricUnit.GRAMS.name()
            if(useMeasurement) {
                quantity.measurementId == measurement.id
                quantity.measurementName == measurement.name
                quantity.quantity == expectedCustom
            } else {
                quantity == null
            }
        }
        where:
        useMeasurement | savedMetric   || expectedCustom
        true           | 300           || 2
        false          | 150           || null
        true           | 75            || 0.5
        true           | 37.5          || 0.25
        true           | 50            || 0.33
    }

}
