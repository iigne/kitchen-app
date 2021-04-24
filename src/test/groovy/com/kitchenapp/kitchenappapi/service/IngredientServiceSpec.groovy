package com.kitchenapp.kitchenappapi.service

import com.kitchenapp.kitchenappapi.dto.ingredient.IngredientDTO
import com.kitchenapp.kitchenappapi.dto.ingredient.MeasurementDTO
import com.kitchenapp.kitchenappapi.model.ingredient.MetricUnit
import com.kitchenapp.kitchenappapi.providers.model.CategoryProvider
import com.kitchenapp.kitchenappapi.providers.model.MeasurementProvider
import com.kitchenapp.kitchenappapi.repository.ingredient.CategoryRepository
import com.kitchenapp.kitchenappapi.repository.ingredient.IngredientRepository
import com.kitchenapp.kitchenappapi.service.ingredient.IngredientService
import com.kitchenapp.kitchenappapi.service.ingredient.MeasurementService
import spock.lang.Specification

class IngredientServiceSpec extends Specification {

    IngredientRepository ingredientRepository = Mock()
    CategoryRepository categoryRepository = Mock()
    MeasurementService measurementService = Mock()

    IngredientService ingredientService

    def setup() {
        ingredientService = new IngredientService(ingredientRepository, categoryRepository, measurementService)
    }

    def "should save ingredient"() {
        given: "DTOs are valid"
        def measurementDTOs = []
        numMeasurements.times { it ->
            measurementDTOs.add(
                    new MeasurementDTO(name: "m" + it, metricUnit: metricUnitString, metricQuantity: it * 100))
        }
        def ingredientDTO = new IngredientDTO(
                name: name, metricUnit: metricUnitString, category: category,
                shelfLifeDays: shelfLifeDays, measurements: measurementDTOs
        )
        when: "create is called"
        def result = ingredientService.create(ingredientDTO)

        then:
        1 * categoryRepository.findByName(category) >> Optional.of(CategoryProvider.make(name: category))
        1 * measurementService.getMetricMeasurement(metricUnitEnum) >>
                MeasurementProvider.make(metricUnit: metricUnitEnum, name: metricUnitString)
        numMeasurements * measurementService.findByNameQuantityUnit(_,_,metricUnitEnum) >> Optional.empty()

        and: "ingredient is saved"
        1 * ingredientRepository.save(ingredient -> {
            ingredient.name == name
            ingredient.metricUnit == metricUnitEnum
            ingredient.shelfLifeDays == savedShelfLife
            ingredient.measurements.size() == numMeasurements + 1
        })

        where:
        name       | metricUnitString | category    | shelfLifeDays | numMeasurements || metricUnitEnum         | savedCategoryId | savedShelfLife
        "Potato"   | "g"              | "Vegetable" | 20            | 1               || MetricUnit.GRAMS       | 1               | 20
        "Oat milk" | "ml"             | "Fridge"    | null          | 2               || MetricUnit.MILLILITRES | 1               | 7

    }

}
