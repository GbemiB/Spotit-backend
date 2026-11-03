package com.spotit.api.common.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record SecurityUser(UUID userId, String email, boolean premium, boolean admin, boolean partner) implements UserDetails {
    public static final String ROLE_PARTNER = "ROLE_PARTNER";

    public SecurityUser(UUID userId, String email, boolean premium, boolean admin) {
        this(userId, email, premium, admin, false);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (premium) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PREMIUM"));
        }
        if (admin) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (partner) {
            authorities.add(new SimpleGrantedAuthority(ROLE_PARTNER));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
