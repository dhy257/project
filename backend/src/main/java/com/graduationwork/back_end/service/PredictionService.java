package com.graduationwork.back_end.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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

    // Flask API로 예측 요청을 보내는 메서드
    public ResponseEntity<String> getPredictionFromFlask(List<String> inputTexts) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // JSON 요청 준비
            String json = objectMapper.writeValueAsString(Map.of("text", inputTexts));

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            // Remove or update the API key if not necessary
            // headers.set("Authorization", "Bearer YOUR_API_KEY");

            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            // Flask API 호출
            ResponseEntity<String> response = restTemplate.exchange(flaskApiUrl, HttpMethod.POST, entity, String.class);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get response from Flask API.", e);
        }
    }
}
