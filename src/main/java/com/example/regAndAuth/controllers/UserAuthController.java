package com.example.regAndAuth.controllers;

import com.example.regAndAuth.dtos.SignupPayload;
import com.example.regAndAuth.err.UserAlreadyExistsException;
import com.example.regAndAuth.models.User;
import com.example.regAndAuth.services.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserAuthController {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public UserAuthController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @ModelAttribute("regErr")
    public String regErr() {
        return "";
    }

    @GetMapping("/login")
    public String handleLogin() {
        return "login";
    }

    @PostMapping("/signup")
    public String handleSignup(SignupPayload registrationForm,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userDetailsService.saveUser(registrationForm);
            request.login(registrationForm.getEmail(), registrationForm.getPassword());
            return "redirect:/";
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("regErr", e.getMessage());
        } catch (ServletException e) {
            redirectAttributes.addFlashAttribute("regErr", "Login error.");
        }
        return "redirect:/login";
    }
}
