package com.omnedu.auth_service.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.omnedu.auth_service.model.User;

public class UserDetailsImpl implements UserDetails {
    @SuppressWarnings("unused")
    private Long id;
    private String username;
    private String password;

    public UserDetailsImpl(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
            user.getId(),
            user.getUsername(),
            user.getPassword()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return empty authorities since we're not using roles
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public User getUser() {
        return new User(id, username, password);
    }

    // Keep other methods unchanged
    // (isAccountNonExpired, isAccountNonLocked, etc.)
    
}
