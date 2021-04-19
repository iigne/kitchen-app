package com.kitchenapp.kitchenappapi.controller

import com.kitchenapp.kitchenappapi.dto.IngredientDTO
import com.kitchenapp.kitchenappapi.dto.MeasurementDTO
import com.kitchenapp.kitchenappapi.model.Category
import com.kitchenapp.kitchenappapi.model.Ingredient
import com.kitchenapp.kitchenappapi.model.MetricUnit
import com.kitchenapp.kitchenappapi.providers.model.IngredientProvider
import com.kitchenapp.kitchenappapi.repository.IngredientRepository
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf

class IngredientControllerIntegrationSpec extends AbstractIntegrationSpec {

    @Autowired
    IngredientRepository ingredientRepository

    def "create new ingredient with measurement"() {
        given: "measurement DTO is valid"
        def measurementDTO = new MeasurementDTO(name: "Slice", metricUnit: "g", metricQuantity: 80)
        and: "ingredient DTO is valid"
        def ingredientDTO = new IngredientDTO(name: "Sourdough Bread", category: "Other", metricUnit: "g", shelfLifeDays: 7, measurements: [measurementDTO])

        when: "a request is made to create new ingredient"
        def result = mvc.perform(post("/ingredient").with(csrf())
                .contentType("application/json")
                .content(toJson(ingredientDTO)))
                .andReturn()
        then: "response is created"
        result.response.status == 201

        and: "response body contains the created entity"
        with(toObject(result.response.contentAsString)) {
            name == "Sourdough Bread"
            metricUnit == MetricUnit.GRAMS.name()
            category == "Other"
            measurements.size() == 2
            measurements*.name.sort() == ["g", "Slice"].sort()
        }
    }

    def "search for ingredient"() {
        given: "there is a list of ingredients in the database"
        def ingredients = [IngredientProvider.make(name:"Ingredient1"), IngredientProvider.make(name:"Ingredient2"), IngredientProvider.make(name:"Ingredient3")]
        ingredientRepository.saveAll(ingredients)
        when: "a fragment is searched for"
        def term = "Ingr"
        def result = mvc.perform(get("/ingredient/search")
                .param("term", term))
                .andReturn()

        then: "ingredients containing the fragment in the name are found"
        with(result.response) {
            status == 200
            contentAsString.contains("Ingredient1")
            contentAsString.contains("Ingredient2")
            contentAsString.contains("Ingredient3")
        }
    }

}
