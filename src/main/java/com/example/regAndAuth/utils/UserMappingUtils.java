package com.example.regAndAuth.utils;

import com.example.regAndAuth.models.CustomOAuth2User;
import com.example.regAndAuth.models.CustomOidcUser;
import com.example.regAndAuth.models.CustomUserDetails;
import com.example.regAndAuth.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserMappingUtils {

    public static CustomUserDetails mapToCustomUserDetails(CustomOAuth2User oAuth2User) {
        User user = new User();
        user.setName(oAuth2User.getName());
        user.setEmail(oAuth2User.getEmail());
        return new CustomUserDetails(user);
    }

    public static CustomUserDetails mapToCustomUserDetails(CustomOidcUser oidcUser) {
        User user = new User();
        user.setName(oidcUser.getFullName());
        user.setEmail(oidcUser.getEmail());
        return new CustomUserDetails(user);
    }
}
