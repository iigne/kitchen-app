package com.kitchenapp.kitchenappapi.controller

import com.kitchenapp.kitchenappapi.dto.QuantityDTO
import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO
import com.kitchenapp.kitchenappapi.repository.IngredientRepository
import com.kitchenapp.kitchenappapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") //TODO create test configuration with these annotations
@WithMockUser()
class UserIngredientControllerIntegrationSpec extends Specification {

    @Autowired
    private MockMvc mvc

    @Autowired
    private IngredientRepository ingredientRepository

    @Autowired
    private UserRepository userRepository


    def "should create new user ingredient"() {
        given: "logged in user exists in database"
        def auth = SecurityContextHolder.getContext().authentication
        def user = userRepository.findByUsername(auth.getName())

        and: "some ingredient exists in the database"
        def ingredient = ingredientRepository.findAll().get(0)

        and: "valid user ingredient DTO"
//        def userIngredientDTO = new UserIngredientDTO(ingredientId: ingredient.getId(),
//                metricQuantity: new QuantityDTO(m))

        when: "a request is made to create new user ingredient"
        then: "status is created "
        and: "the response body contains the created entity"
    }

    def "should edit user ingredient"() {
        given: "user ingredient exists in the db"
        and: "user ingredient DTO is valid"
        when: "a request is made to edit user ingredient"
        then: "status is ok"
        and: "the response body contains the edited entity"
    }

    def "should delete user ingredient"() {
        given: "user ingredient exists in the db"
        when: "a request is made to delete user ingredient"
        then: "status is deleted"
    }

    def "should display user ingredients"() {
        given: "user ingredients exist in the db"
        when: "request is made to display all user ingredients by user"
        then: "status is ok"
        and: "response body contains user ingredients"
    }

}
