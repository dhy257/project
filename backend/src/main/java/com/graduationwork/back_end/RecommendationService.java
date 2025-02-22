package com.graduationwork.back_end;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {
    private final RestTemplate restTemplate;
    private final String FLASK_API_BASE_URL = "http://localhost:8000/recommend";

    public RecommendationService() {
        this.restTemplate = new RestTemplate();
    }

    public List<String> getUsedRecommendations(int userId) {
        String url = FLASK_API_BASE_URL + "/used/" + userId;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("recommended_ingredients")) {
            return (List<String>) response.getBody().get("recommended_ingredients");
        }
        return List.of();
    }

    public List<String> getWastedRecommendations(int userId) {
        String url = FLASK_API_BASE_URL + "/wasted/" + userId;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("recommended_wasted_ingredients")) {
            return (List<String>) response.getBody().get("recommended_wasted_ingredients");
        }
        return List.of();
    }
}
