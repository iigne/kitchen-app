package com.kitchenapp.kitchenappapi.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class JwtUserDetails implements UserDetails {

    private final int id;

    private final String username;

    private final String email;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public static JwtUserDetails build(User user) {
        List<GrantedAuthority> authorityList = Collections.singletonList(
                new SimpleGrantedAuthority("USER"));
        return new JwtUserDetails(user.getId(), user.getUsername(),
                user.getEmail(), user.getPassword(), authorityList);
    }

    //TODO implement this
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
