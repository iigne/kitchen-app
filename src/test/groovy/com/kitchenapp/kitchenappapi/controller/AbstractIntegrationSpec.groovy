package com.kitchenapp.kitchenappapi.controller

import com.kitchenapp.kitchenappapi.dto.IngredientDTO
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class AbstractIntegrationSpec extends Specification {
    @Autowired
    protected MockMvc mvc

    protected static String toJson(object) {
        return new ObjectMapper().writeValueAsString(object)
    }

    protected static IngredientDTO toObject(json) {
        return new ObjectMapper().readValue(json, IngredientDTO.class)
    }

}
