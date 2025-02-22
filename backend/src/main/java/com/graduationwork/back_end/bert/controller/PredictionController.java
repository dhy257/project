package com.graduationwork.back_end.bert.controller;

import com.graduationwork.back_end.bert.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/predict")
public class PredictionController {

    private static final Logger logger = LoggerFactory.getLogger(PredictionController.class);

    private final PredictionService predictionService;

    @Autowired
    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    // Modify the method to accept the 'text' field as part of the request body
    @PostMapping
    public ResponseEntity<String> getPrediction(@RequestBody Map<String, Object> requestBody) {
        Object textValue = requestBody.get("text");
        List<String> texts;

        if (textValue instanceof String) {
            texts = List.of((String) textValue);
        } else if (textValue instanceof List) {
            texts = (List<String>) textValue;
        } else {
            texts = List.of();
        }

        logger.info("Received request with texts: {}", texts);
        return predictionService.getPredictionFromFlask(texts);
    }

}
