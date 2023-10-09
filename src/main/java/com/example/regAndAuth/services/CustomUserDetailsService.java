package com.example.regAndAuth.services;

import com.example.regAndAuth.dtos.ProfilePayload;
import com.example.regAndAuth.dtos.SignupPayload;
import com.example.regAndAuth.dtos.UserDto;
import com.example.regAndAuth.err.CurrentUserNotFoundException;
import com.example.regAndAuth.err.UserAlreadyExistsException;
import com.example.regAndAuth.models.CustomUserDetails;
import com.example.regAndAuth.models.User;
import com.example.regAndAuth.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getCurrentUserDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails userDetails =
                    (CustomUserDetails) authentication.getPrincipal();
            UserDto userDto = modelMapper.map(userDetails.getUser(), UserDto.class);
            userDto.setRole(userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(r->r.replace("ROLE_", ""))
                    .collect(Collectors.joining(",")));
            return userDto;
        } else {
            UserDto userDto = new UserDto();
            return userDto;
        }
    }

    public Boolean changeUserData(ProfilePayload profilePayload) throws UserAlreadyExistsException, CurrentUserNotFoundException {
        Boolean userIsChanged = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails userDetails =
                    (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            String newUserEmail = profilePayload.getEmail();
            if (newUserEmail != null && !newUserEmail.equals("") && !newUserEmail.equals(user.getEmail())) {
                User userByEmail = userRepository.findUserByEmail(newUserEmail);
                if (userByEmail == null) {
                    user.setEmail(newUserEmail);
                    userIsChanged = true;
                } else {
                    throw new UserAlreadyExistsException("This email is already associated with an account. Please choose another email.");
                }
            }
            String newUserName = profilePayload.getName();
            if (newUserName != null && !newUserName.equals("") && !newUserName.equals(user.getName())) {
                user.setName(newUserName);
                userIsChanged = true;
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String newUserPassword = profilePayload.getPassword();
            if (newUserPassword != null && !newUserPassword.equals("") && !passwordEncoder.matches(newUserPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newUserPassword));
                userIsChanged = true;
            }
            if (userIsChanged) {
                userRepository.save(user);
            }
        } else {
            throw new CurrentUserNotFoundException("User doesn't found.");
        }
        return userIsChanged;
    }

    public User saveUser(SignupPayload payload) throws UserAlreadyExistsException {
        User user = userRepository.findUserByEmail(payload.getEmail());
        if (user == null) {
            user = modelMapper.map(payload, User.class);
            String newEncodedPassword = new BCryptPasswordEncoder().encode(payload.getPassword());
            user.setPassword(newEncodedPassword);
            User newUser = userRepository.save(user);
            return newUser;
        } else {
            throw new UserAlreadyExistsException("This email is already associated with an account.");
        }
    }
}
