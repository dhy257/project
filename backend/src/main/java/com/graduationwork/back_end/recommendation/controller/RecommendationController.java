package com.graduationwork.back_end.recommendation.controller;

import com.graduationwork.back_end.recommendation.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final RestTemplate restTemplate;
    private static final String FLASK_URL = "http://localhost:8000/recommend";

    public RecommendationController(RecommendationService recommendationService, RestTemplate restTemplate) {
        this.recommendationService = recommendationService;
        this.restTemplate = restTemplate;
    }

    // 사용자가 사용한 식재료 추천 요청 시 Flask로 요청
    @GetMapping("/used/{userId}")
    public ResponseEntity<?> getUsedRecommendation(@PathVariable int userId) {
        String flaskEndpoint = FLASK_URL + "/used/" + userId;
        ResponseEntity<Map> response = restTemplate.getForEntity(flaskEndpoint, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to get recommendations from Flask");
        }
    }

    // 사용자가 버린 식재료 추천 요청 시 Flask로 요청
    @GetMapping("/wasted/{userId}")
    public ResponseEntity<?> getWastedRecommendation(@PathVariable int userId) {
        String flaskEndpoint = FLASK_URL + "/wasted/" + userId;
        ResponseEntity<Map> response = restTemplate.getForEntity(flaskEndpoint, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to get recommendations from Flask");
        }
    }

    // Flask에서 추천 결과를 저장 요청
    @PostMapping("/save")
    public ResponseEntity<String> saveRecommendations(@RequestBody Map<String, Object> recommendationData) {
        recommendationService.saveRecommendations(recommendationData);
        return ResponseEntity.ok("Recommendations saved successfully");
    }

    // 모든 사용자의 사용한 식재료 데이터 반환 (Flask 학습용)
    @GetMapping("/used/all")
    public List<Map<String, Object>> getAllUsedData() {
        return recommendationService.getAllUsedData();
    }

    // 모든 사용자의 버린 식재료 데이터 반환 (Flask 학습용)
    @GetMapping("/wasted/all")
    public List<Map<String, Object>> getAllWastedData() {
        return recommendationService.getAllWastedData();
    }
}
