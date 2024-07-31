package com.iftttse.backend.controller;

import com.iftttse.backend.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predict")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @GetMapping("/{appletId}")
    public Object predict(@PathVariable Long appletId) {
        return predictionService.predict(appletId);
    }
}
