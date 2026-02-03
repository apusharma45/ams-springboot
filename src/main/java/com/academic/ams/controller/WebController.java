package com.academic.ams.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String root() {
        return "redirect:/web/home";
    }

    @GetMapping("/web/login")
    public String login() {
        return "web/login";
    }

    @GetMapping("/web/home")
    public String home(Authentication auth, Model model) {
        String userId = auth.getName();
        String role = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"))
                ? "TEACHER" : "STUDENT";

        model.addAttribute("userId", userId);
        model.addAttribute("role", role);
        return "web/home";
    }
}
