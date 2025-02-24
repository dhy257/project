package com.graduationwork.back_end.recommendation.service;

import com.graduationwork.back_end.recommendation.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    // 모든 사용자의 사용한 식재료 데이터를 반환 (Flask 학습용)
    public List<Map<String, Object>> getAllUsedData() {
        List<Map<String, Object>> usedData = recommendationRepository.findAllUsedData();
        System.out.println("✅ [Spring] Flask 서버로 전달할 사용한 식재료 데이터: " + usedData);
        return usedData;
    }

    // 모든 사용자의 버린 식재료 데이터를 반환 (Flask 학습용)
    public List<Map<String, Object>> getAllWastedData() {
        List<Map<String, Object>> wastedData = recommendationRepository.findAllWastedData();
        System.out.println("✅ [Spring] Flask 서버로 전달할 버린 식재료 데이터: " + wastedData);
        return wastedData;
    }

    // Flask에서 전달받은 추천 결과를 DB에 저장
    public void saveRecommendations(Map<String, Object> recommendationData) {
        int userId = (int) recommendationData.get("user_id");
        String recommendationType = (String) recommendationData.get("recommendation_type");
        List<String> recommendedIngredients = (List<String>) recommendationData.get("recommended_ingredients");

        System.out.println("📌 [Spring] Flask에서 추천 데이터를 받았습니다.");
        System.out.println("📌 [Spring] 사용자 ID: " + userId);
        System.out.println("📌 [Spring] 추천 유형: " + recommendationType);
        System.out.println("📌 [Spring] 추천된 식재료: " + recommendedIngredients);

        recommendationRepository.saveRecommendations(userId, recommendationType, recommendedIngredients);
        System.out.println("✅ [Spring] 추천 데이터를 데이터베이스에 성공적으로 저장했습니다.");
    }
}
