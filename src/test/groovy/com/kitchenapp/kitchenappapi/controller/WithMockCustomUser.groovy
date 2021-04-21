package com.kitchenapp.kitchenappapi.controller;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * annotation to use for tests when whole of the custom principal (inc. id) is required in test
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "admin";

    String password() default "password";

    String email() default "email@email.com";

    int id() default 1;
}