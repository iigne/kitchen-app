package com.kitchenapp.kitchenappapi.controller

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") //TODO create test configuration with these annotations
@WithMockUser
class UserIngredientControllerIntegrationSpec extends Specification {

    def "should create new user ingredient"() {
        given: "user and ingredient exists in database"

        and: "valid user ingredient DTO"

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
