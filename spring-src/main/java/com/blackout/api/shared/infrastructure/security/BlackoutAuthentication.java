package com.blackout.api.shared.infrastructure.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * Custom authentication token carrying bandId + role extracted from JWT.
 * The JWT payload mirrors the NestJS shape: { sub, username, role, bandId }
 */
public class BlackoutAuthentication extends AbstractAuthenticationToken {

    private final String userId;
    private final String username;
    private final String role;
    private final String bandId;

    public BlackoutAuthentication(String userId, String username, String role, String bandId) {
        super(List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.bandId = bandId;
        setAuthenticated(true);
    }

    @Override public Object getCredentials() { return null; }
    @Override public Object getPrincipal() { return userId; }

    public String getUserId()   { return userId; }
    public String getUsername() { return username; }
    public String getRole()     { return role; }
    public String getBandId()   { return bandId; }
}
