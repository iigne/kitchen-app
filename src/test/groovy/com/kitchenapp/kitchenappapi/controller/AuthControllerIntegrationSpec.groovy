package com.kitchenapp.kitchenappapi.controller

import com.kitchenapp.kitchenappapi.dto.user.RegisterDTO

import static com.kitchenapp.kitchenappapi.controller.JsonParseHelper.toApiError
import static com.kitchenapp.kitchenappapi.controller.JsonParseHelper.toJson
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class AuthControllerIntegrationSpec extends AbstractIntegrationSpec {

    def "should fail to register"() {
        given: "user with desired username already exists"
        def existingUser = getAnotherUser()

        and: "register request is valid"
        def registerRequest = new RegisterDTO(username: existingUser.username,
                email: "totallynewemail@email.com", password: "verysecurepassword")

        when: "someone attempts to register and uses existing username"
        def result = mvc.perform(post("/auth/register")
                .with(csrf())
                .contentType("application/json")
                .content(toJson(registerRequest)))
                .andReturn()

        then: "response status is 400 bad request and explanation is given"
        result.response.status == 400
        with(toApiError(result.response.contentAsString)) {
            errorMessage == "Username or email already in use"
        }

        and: "user has not been created"
        userRepository.findAll().size() == 1
    }

}
