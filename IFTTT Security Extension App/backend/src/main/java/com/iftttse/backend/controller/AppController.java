package com.iftttse.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Controller
public class AppController {

    private final WebClient webClient;

    public AppController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.ifttt.com/v1").build();
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/get-user-applets")
    public String getUserApplets(@AuthenticationPrincipal OAuth2AuthenticationToken authentication, Model model) {
        String token = authentication.getPrincipal().getAttribute("access_token");

        // Preleva le applets dell'utente
        Map<String, Object> applets = this.webClient.get()
                .uri("/user/applets")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // Esegui la predizione con il modello AI (funzione fittizia, implementare come necessario)
        Map<String, String> predictions = predictSecurity(applets);

        model.addAttribute("applets", applets);
        model.addAttribute("predictions", predictions);
        return "dashboard";
    }

    private Map<String, String> predictSecurity(Map<String, Object> applets) {
        // Implementa la logica del modello AI qui
        // Restituisci un mapping delle applets con le rispettive predizioni di sicurezza
        // Esempio fittizio:
        return Map.of("applet1", "safe", "applet2", "unsafe");
    }
}
