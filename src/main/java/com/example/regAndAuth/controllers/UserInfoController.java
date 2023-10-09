package com.example.regAndAuth.controllers;

import com.example.regAndAuth.dtos.UserDto;
import com.example.regAndAuth.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserInfoController {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public UserInfoController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @ModelAttribute("currentUser")
    public UserDto currentUser() {
        return userDetailsService.getCurrentUserDto();
    }
}
