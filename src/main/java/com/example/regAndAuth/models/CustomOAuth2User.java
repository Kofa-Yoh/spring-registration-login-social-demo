package com.example.regAndAuth.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * Authenticated user from Facebook
 **/
public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oauth2User;
    private String clientName;

    @Autowired
    public CustomOAuth2User(OAuth2User oauth2User, String clientName) {
        this.oauth2User = oauth2User;
        this.clientName = clientName;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail() {
        return oauth2User.<String>getAttribute("email");
    }

    public String getClientName() {
        return this.clientName;
    }
}
