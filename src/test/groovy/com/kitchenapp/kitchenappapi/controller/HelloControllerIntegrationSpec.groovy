package com.kitchenapp.kitchenappapi.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") //TODO create test configuration with these annotations
class HelloControllerIntegrationSpec extends Specification {
    @Autowired
    private MockMvc mvc

    def "should fail to call hello endpoint for unauthenticated user"() {

        when: "hello endpoint is called"
        def result = mvc.perform(get("/secured-hello"))
                .andReturn()

        then: "should be unauthorised"
        with(result.response) {
            status == 401
        }
    }
}
