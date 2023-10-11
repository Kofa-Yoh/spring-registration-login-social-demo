package com.example.regAndAuth.components;

import com.example.regAndAuth.models.AuthenticationType;
import com.example.regAndAuth.models.CustomOAuth2User;
import com.example.regAndAuth.models.CustomOidcUser;
import com.example.regAndAuth.models.User;
import com.example.regAndAuth.repositories.UserRepository;
import com.example.regAndAuth.utils.UserMappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationEvents(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        Object principal = success.getAuthentication().getPrincipal();
        if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
            this.saveUserInDb(UserMappingUtils.mapToCustomUserDetails(oAuth2User).getUser(), AuthenticationType.valueOf(oAuth2User.getClientName().toUpperCase()));
        } else if (principal instanceof CustomOidcUser) {
            CustomOidcUser oidcUser = (CustomOidcUser) principal;
            this.saveUserInDb(UserMappingUtils.mapToCustomUserDetails(oidcUser).getUser(), AuthenticationType.valueOf(oidcUser.getClientName().toUpperCase()));
        }
    }

    private void saveUserInDb(User authUser, AuthenticationType authType) {
        User userByEmail = userRepository.findUserByEmail(authUser.getEmail());
        if (userByEmail == null) {
            User user = new User();
            user.setName(authUser.getName());
            user.setEmail(authUser.getEmail());
            user.setAuthType(authType);
            userRepository.save(user);
        }
    }
}
