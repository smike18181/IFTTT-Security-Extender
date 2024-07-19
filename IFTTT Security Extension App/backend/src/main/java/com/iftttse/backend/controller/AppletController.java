package com.iftttse.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class AppletController {

    @Value("${ifttt.api.url}")
    private String iftttApiUrl;

    @GetMapping("/get-user-applets")
    public String getUserApplets(Model model, @RegisteredOAuth2AuthorizedClient("ifttt") OAuth2AuthorizedClient authorizedClient) {
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(iftttApiUrl, HttpMethod.GET, entity, String.class);
        model.addAttribute("applets", response.getBody());
        return "applets"; // Crea una semplice pagina per visualizzare le applets
    }
}
