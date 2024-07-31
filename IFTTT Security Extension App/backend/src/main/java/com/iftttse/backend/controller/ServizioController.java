package com.iftttse.backend.controller;

import com.iftttse.backend.entity.Servizio;
import com.iftttse.backend.service.ServizioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServizioController {
    private final ServizioService servizioService;

    @Autowired
    public ServizioController(ServizioService servizioService) {
        this.servizioService = servizioService;
    }

    @GetMapping
    public List<Servizio> getAllServices() {
        return servizioService.getAllServices();
    }
    @GetMapping("/search")
    public List<Servizio> getServizi(@RequestParam String name) {

        if(name.isEmpty())    return null;
        return servizioService.getServicesByName(name);

    }



}
