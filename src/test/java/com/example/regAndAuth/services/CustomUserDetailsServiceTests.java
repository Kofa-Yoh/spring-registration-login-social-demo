package com.example.regAndAuth.services;

import com.example.regAndAuth.dtos.ProfilePayload;
import com.example.regAndAuth.dtos.SignupPayload;
import com.example.regAndAuth.dtos.UserDto;
import com.example.regAndAuth.err.CurrentUserNotFoundException;
import com.example.regAndAuth.err.UserAlreadyExistsException;
import com.example.regAndAuth.models.User;
import com.example.regAndAuth.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class CustomUserDetailsServiceTests {

    public static final String TESTING_EMAIL = "testing1@gmail.com";
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    CustomUserDetailsServiceTests(UserRepository userRepository, CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Test
    @WithUserDetails(TESTING_EMAIL)
    void getCurrentUserDto() {
        UserDto userDto = userDetailsService.getCurrentUserDto();
        assertNotNull(userDto);
        assertTrue(TESTING_EMAIL.equals(userDto.getEmail()));
        assertTrue(userDto.getRole().contains("USER"));
    }

    @Test
    @WithUserDetails(TESTING_EMAIL)
    void changeUserDataFail() {
        ProfilePayload profilePayload = new ProfilePayload();
        profilePayload.setPassword("");
        Boolean changeResult = null;
        try {
            changeResult = userDetailsService.changeUserData(profilePayload);
        } catch (UserAlreadyExistsException | CurrentUserNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(changeResult);
        assertFalse(changeResult);
    }

    @Test
    @WithUserDetails(TESTING_EMAIL)
    void changeUserDataSuccess() {
        ProfilePayload profilePayload = new ProfilePayload();
        String newPassword = "password";
        profilePayload.setPassword(newPassword);
        Boolean changeResult = null;
        try {
            changeResult = userDetailsService.changeUserData(profilePayload);
        } catch (UserAlreadyExistsException | CurrentUserNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(changeResult);
        User user = userRepository.findUserByEmail(TESTING_EMAIL);
        assertTrue(new BCryptPasswordEncoder().matches(newPassword, user.getPassword()));
    }

    @Test
    void saveUserFail() {
        SignupPayload payload = new SignupPayload();
        payload.setEmail(TESTING_EMAIL);
        payload.setName("New name");
        payload.setPassword("New password");
        assertThrows(UserAlreadyExistsException.class, () -> userDetailsService.saveUser(payload));
    }

    @Test
    void saveUserSuccess() {
        SignupPayload payload = new SignupPayload();
        payload.setEmail("newemail@gmail.com");
        payload.setName("New name");
        payload.setPassword("New password");

        User userNotExist = userRepository.findUserByEmail(payload.getEmail());
        assertNull(userNotExist);

        User user = null;
        try {
            user = userDetailsService.saveUser(payload);
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        }
        assertNotNull(user);
        assertTrue(payload.getEmail().equals(user.getEmail()));
        assertTrue(payload.getName().equals(user.getName()));
        assertTrue(new BCryptPasswordEncoder().matches(payload.getPassword(), user.getPassword()));

        User userFromRepository = userRepository.findUserByEmail(payload.getEmail());
        assertNotNull(userFromRepository);
    }
}