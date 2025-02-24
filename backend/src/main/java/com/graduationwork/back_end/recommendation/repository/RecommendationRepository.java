package com.graduationwork.back_end.recommendation.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RecommendationRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 모든 사용자의 사용한 식재료 데이터 조회 (Flask 학습용)
    public List<Map<String, Object>> findAllUsedData() {
        String sql = "SELECT user_id, ingredient_name FROM used";
        List<Map<String, Object>> usedData = jdbcTemplate.queryForList(sql);
        System.out.println("📌 [Spring] DB에서 사용한 식재료 데이터 조회: " + usedData);
        return usedData;
    }

    // 모든 사용자의 버린 식재료 데이터 조회 (Flask 학습용)
    public List<Map<String, Object>> findAllWastedData() {
        String sql = "SELECT user_id, ingredient_name FROM wasted";
        List<Map<String, Object>> wastedData = jdbcTemplate.queryForList(sql);
        System.out.println("📌 [Spring] DB에서 버린 식재료 데이터 조회: " + wastedData);
        return wastedData;
    }

    // 추천 결과를 DB에 저장
    public void saveRecommendations(int userId, String recommendationType, List<String> recommendedIngredients) {
        System.out.println("📌 [Spring] 기존 추천 데이터를 삭제합니다. (사용자 ID: " + userId + ", 추천 유형: " + recommendationType + ")");
        String deleteSql = "DELETE FROM recommended WHERE user_id = ? AND recommendation_type = ?";
        jdbcTemplate.update(deleteSql, userId, recommendationType);

        System.out.println("✅ [Spring] 새로운 추천 데이터를 저장합니다. (사용자 ID: " + userId + ", 추천 유형: " + recommendationType + ")");
        String insertSql = "INSERT INTO recommended (user_id, ingredient_name, recommendation_type) VALUES (?, ?, ?)";

        for (String ingredient : recommendedIngredients) {
            jdbcTemplate.update(insertSql, userId, ingredient, recommendationType);
            System.out.println("✅ [Spring] 저장된 추천 데이터 - 사용자 ID: " + userId + ", 식재료: " + ingredient + ", 추천 유형: " + recommendationType);
        }

        System.out.println("✅ [Spring] 모든 추천 데이터를 성공적으로 저장했습니다! (사용자 ID: " + userId + ")");
    }
}
