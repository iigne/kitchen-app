package com.kitchenapp.kitchenappapi.controller;

import com.kitchenapp.kitchenappapi.model.JwtUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

/**
 * Class to provide authentication for tests
 *
 * Creates a custom security context, taking data from <code>WithMockCustomUser</code>.
 * Main purpose of this is to give the correct <code>UserDetails</code> to methods that use it
 * (for example, fetching authenticated user's ID in the controllers)
 */
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        JwtUserDetails principal = new JwtUserDetails(customUser.id(), customUser.username(), customUser.email(),
                customUser.password(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
