package com.graduationwork.back_end.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class PredictionService {

    private final RestTemplate restTemplate;

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    public PredictionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Modified to handle a list of items
    public ResponseEntity<String> getPredictionFromFlask(List<String> inputTexts) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert the list of items to JSON with the "items" key
            String json = objectMapper.writeValueAsString(Map.of("text", inputTexts));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            // Send the request to Flask API
            ResponseEntity<String> response = restTemplate.exchange(flaskApiUrl, HttpMethod.POST, entity, String.class);
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get response from Flask API.", e);
        }
    }
}
