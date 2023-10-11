package com.example.regAndAuth.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

/**
 * Authenticated user from Google
 **/
public class CustomOidcUser implements OidcUser {

    private final OidcUser oidcUser;

    private final String clientName;

    @Autowired
    public CustomOidcUser(OidcUser oidcUser, String clientName) {
        this.oidcUser = oidcUser;
        this.clientName = clientName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oidcUser.getAuthorities();
    }

    @Override
    public String getFullName() {
        return oidcUser.getFullName();
    }

    @Override
    public String getName() {
        return oidcUser.getName();
    }

    @Override
    public String getEmail() {
        return oidcUser.getEmail();
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    public OidcUser getOidcUser() {
        return oidcUser;
    }

    public String getClientName() {
        return clientName;
    }
}
