package com.graduationwork.back_end;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recommend")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    //  사용한 식재료 추천 API
    @GetMapping("/used/{userId}")
    public List<String> getUsedRecommendations(@PathVariable int userId) {
        return recommendationService.getUsedRecommendations(userId);
    }

    //  낭비한 식재료 추천 API
    @GetMapping("/wasted/{userId}")
    public List<String> getWastedRecommendations(@PathVariable int userId) {
        return recommendationService.getWastedRecommendations(userId);
    }
}
