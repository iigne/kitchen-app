package com.kitchenapp.kitchenappapi.service

import com.kitchenapp.kitchenappapi.config.JwtTokenUtil
import com.kitchenapp.kitchenappapi.error.UserAlreadyExistsException
import com.kitchenapp.kitchenappapi.payload.RegisterRequest
import com.kitchenapp.kitchenappapi.providers.model.UserProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class AuthServiceSpec extends Specification {

    UserService userService = Mock()
    JwtTokenUtil jwtTokenUtil = Mock()

    PasswordEncoder passwordEncoder = Mock()
    AuthenticationManager authenticationManager = Mock()

    AuthService authService

    def setup() {
        authService = new AuthService(userService, jwtTokenUtil, passwordEncoder, authenticationManager)
    }

    def "should register"() {
        given: "register request with registration details"
        def registerRequest = new RegisterRequest(username: username, password: password, email: email)

        when: "register request made"
        authService.register(registerRequest)

        then: "details are validated and user saved"
        1 * userService.isUsernameOrEmailTaken(username, email) >> false
        1 * passwordEncoder.encode(password) >> encodedPassword

        1 * userService.save(username, email, encodedPassword)

        where:
        username| password | email    | encodedPassword
        "user" | "password" | "email" | "totally-encrypted-password"
    }

    def "should fail to register"() {
        given: "register request with duplicate registration details"
        def registerRequest = new RegisterRequest(username: username, email: email, password: "password")

        when: "register request made"
        authService.register(registerRequest)

        then: "validations fail"
        1 * userService.isUsernameOrEmailTaken(username, email) >> true

        and: "exception is thrown"
        thrown(UserAlreadyExistsException)

        where:
        username | email
        "existing" | "user@test.com"
    }

}
