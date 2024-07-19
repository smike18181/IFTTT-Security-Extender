package com.iftttse.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String home() {
        return "home"; // Crea una semplice pagina di login
    }

    @GetMapping("/oauth2/callback")
    public String callback() {
        return "redirect:/get-user-applets";
    }
}
