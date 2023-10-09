package com.example.regAndAuth.controllers;

import com.example.regAndAuth.dtos.ProfilePayload;
import com.example.regAndAuth.err.CurrentUserNotFoundException;
import com.example.regAndAuth.err.UserAlreadyExistsException;
import com.example.regAndAuth.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainPageController {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public MainPageController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @ModelAttribute("changeDataResult")
    public String regErr() {
        return "";
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @PostMapping("/changeProfile")
    public String changeProfile(ProfilePayload profilePayload, RedirectAttributes redirectAttributes) {
        try {
            Boolean isChanged = userDetailsService.changeUserData(profilePayload);
            if (isChanged) {
                redirectAttributes.addFlashAttribute("changeDataResult", "Data changed.");
            } else {
                redirectAttributes.addFlashAttribute("changeDataResult", "Data didn't change.");
            }
        } catch (CurrentUserNotFoundException | UserAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("changeDataResult", e.getMessage());
        }
        return "redirect:/";
    }
}
