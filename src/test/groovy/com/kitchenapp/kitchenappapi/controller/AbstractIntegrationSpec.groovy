package com.kitchenapp.kitchenappapi.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.kitchenapp.kitchenappapi.dto.IngredientDTO
import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO
import com.kitchenapp.kitchenappapi.model.Ingredient
import com.kitchenapp.kitchenappapi.model.User
import com.kitchenapp.kitchenappapi.providers.model.IngredientProvider
import com.kitchenapp.kitchenappapi.providers.model.UserProvider
import com.kitchenapp.kitchenappapi.repository.IngredientRepository
import com.kitchenapp.kitchenappapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

/**
 * Abstract class containing configuration and common methods for integration tests.
 *
 * Note: the <code>@Sql</code> annotation resets numbering so that we always have
 * the same ID for the test user.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(statements = "ALTER TABLE [user] ALTER COLUMN id RESTART WITH 666")
abstract class AbstractIntegrationSpec extends Specification {

    static final int MOCK_USER_ID = 666

    @Autowired
    protected MockMvc mvc

    @Autowired
    IngredientRepository ingredientRepository

    @Autowired
    private UserRepository userRepository

    def setup() {
        userRepository.deleteAll()
    }

    protected static String toJson(object) {
        return new ObjectMapper().writeValueAsString(object)
    }

    protected static IngredientDTO toIngredientDTO(json) {
        return new ObjectMapper().readValue(json, IngredientDTO.class)
    }

    protected static List<UserIngredientDTO> toUserIngredientDTOList(json) {
        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        return objectMapper.readValue(json, new TypeReference<List<UserIngredientDTO>>() {})
    }

    protected List<Ingredient> createIngredients() {
        def ingredients = [IngredientProvider.make(name:"Ingredient1"), IngredientProvider.make(name:"Ingredient2"), IngredientProvider.make(name:"Ingredient3")]
        return ingredientRepository.saveAll(ingredients)
    }

    protected User getUser() {
        def auth = SecurityContextHolder.getContext().authentication
        return userRepository.findById(MOCK_USER_ID).orElse(
                userRepository.save(UserProvider.make(username: auth.getName())))
    }

}
