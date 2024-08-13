package com.ac.su.user;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    GENERAL,
    ADMIN;
    @Override
    public String getAuthority() {
        return name();
    }
}
