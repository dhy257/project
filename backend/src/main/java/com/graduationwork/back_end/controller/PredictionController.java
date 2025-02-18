package com.graduationwork.back_end.controller;

import com.graduationwork.back_end.service.PredictionService;
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
    public ResponseEntity<String> getPrediction(@RequestBody Map<String, List<String>> requestBody) {
        List<String> texts = requestBody.get("text");  // Extract the list of texts from the request body
        logger.info("Received request with texts: {}", texts);  // Log the received list of items

        // Pass the list of texts to the service method
        return predictionService.getPredictionFromFlask(texts);
    }
}
