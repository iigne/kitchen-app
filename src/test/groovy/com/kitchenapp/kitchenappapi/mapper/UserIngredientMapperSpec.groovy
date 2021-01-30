package com.kitchenapp.kitchenappapi.mapper

import com.kitchenapp.kitchenappapi.dto.QuantityDTO
import com.kitchenapp.kitchenappapi.model.MetricUnit
import com.kitchenapp.kitchenappapi.providers.dto.UserIngredientDTOProvider
import com.kitchenapp.kitchenappapi.providers.model.IngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.MeasurementProvider
import com.kitchenapp.kitchenappapi.providers.model.UserIngredientProvider
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
        def dto = UserIngredientDTOProvider.make(quantity: quantity, metricQuantity: metric,
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
        useMeasurement | inputQuantity | inputExpiry              | inputAdded                 || expectedMetric | expectedAdded              | expectedExpiry
        true           | 2             | null                     | null                       || 2 * 150        | LocalDate.now()            | LocalDate.now().plusDays(14)
        false          | 150           | LocalDate.of(2021, 5, 5) | LocalDate.of(2020, 12, 31) || 150            | LocalDate.of(2020, 12, 31) | LocalDate.of(2021, 5, 5)

    }

    @Unroll
    def "should correctly convert to DTO"() {
        given: "valid ingredient and measurement"
        def measurement = MeasurementProvider.make()
        def ingredient = IngredientProvider.make(measurements: [measurement])

        and: "valid UserIngredient"
        def entity = UserIngredientProvider.make(ingredient: ingredient, customMeasurement: measurement, metricQuantity: savedMetric)

        when: "convert to DTO"
        def dto = UserIngredientMapper.toDTO(entity)

        then:
        with(dto) {
            metricQuantity.quantity == savedMetric
            metricQuantity.measurementName == MetricUnit.GRAMS.name()
            quantity.measurementId == measurement.id
            quantity.measurementName == measurement.name
            quantity.quantity == expectedCustom
        }
        where:
        savedMetric || expectedCustom
        300         || 2
        75          || 0.5
        37.5        || 0.25
        50          || 0.33
    }

}
