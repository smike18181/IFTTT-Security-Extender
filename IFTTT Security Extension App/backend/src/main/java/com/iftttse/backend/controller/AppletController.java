package com.iftttse.backend.controller;

import com.iftttse.backend.entity.Applet;
import com.iftttse.backend.service.AppletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applets")
public class AppletController {

    private final AppletService appletService;

    @Autowired
    public AppletController(AppletService appletService) {
        this.appletService = appletService;
    }

    //http://localhost:8080/api/applets/search?name=Apple
    @GetMapping("/search")
    public List<Applet> getApplets(@RequestParam String name) {

        if(name.isEmpty())    return null;
        return appletService.getAppletsByName(name);

    }

    @GetMapping("/by-canale-id/{id}")
    public List<Applet> getAppletsByCanaleId(@PathVariable Long id) {

        if(id == null)   return null;
        return appletService.getAppletsByCanaleId(id);

    }






}
