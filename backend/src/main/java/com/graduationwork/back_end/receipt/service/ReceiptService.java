package com.graduationwork.back_end.receipt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduationwork.back_end.receipt.ReceiptOcr;
import com.graduationwork.back_end.receipt.dto.PredictedItemDto;
import com.graduationwork.back_end.receipt.model.Receipt;
import com.graduationwork.back_end.receipt.repository.ReceiptRepository;
import com.graduationwork.back_end.user.User;
import com.graduationwork.back_end.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private UserRepository userRepository; // ✅ UserRepository 추가

    @Autowired
    private ReceiptOcr receiptOcr;

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    /**
     * 1. 파일 저장 → OCR 텍스트 추출 → Receipt DB 저장
     * 2. OCR 텍스트에서 상품명만 파싱
     * 3. 각 상품명에 대해 /predict API 호출로 카테고리 예측
     * 4. 예측 결과(PredictedItemDto 리스트) 반환
     */
    public List<PredictedItemDto> processAndPredict(MultipartFile file, Long userId) throws IOException {
        // ✅ userId로 User 객체 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + userId));

        // 파일 저장
        String imagePath = saveImage(file);

        // OCR 처리
        File savedFile = new File(imagePath);
        String ocrText = receiptOcr.extractTextFromImage(savedFile);

        System.out.println("OCR에서 추출한 텍스트: " + ocrText);

        // Receipt 엔티티 생성 및 DB 저장
        Receipt receipt = new Receipt();
        receipt.setUser(user); // ✅ User 객체 설정
        receipt.setReceiptImage(imagePath);
        receipt.setReceiptText(ocrText);
        Receipt savedReceipt = receiptRepository.save(receipt);

        // OCR 텍스트에서 상품명만 파싱 ("상품명   수량   금액" 형태)
        List<String> productNames = parseProductNames(ocrText);

        // 각 상품명마다 /predict API 호출하여 예측 카테고리 획득
        List<PredictedItemDto> predictedItems = new ArrayList<>();
        for (String productName : productNames) {
            String predictedCategory = predictCategory(productName);
            predictedItems.add(new PredictedItemDto(savedReceipt.getId(), productName, predictedCategory));
        }

        return predictedItems;
    }

    // 파일을 로컬에 저장 (디렉토리 없으면 생성)
    private String saveImage(MultipartFile file) throws IOException {
        // ✅ MacOS에서 접근 가능한 절대 경로 설정
        String userHome = System.getProperty("user.home"); // 사용자의 홈 디렉토리
        String uploadPath = userHome + File.separator + "uploads"; // ~/uploads 디렉토리

        File uploadDirectory = new File(uploadPath);

        // ✅ 디렉토리가 없으면 생성
        if (!uploadDirectory.exists()) {
            boolean isCreated = uploadDirectory.mkdirs();
            if (!isCreated) {
                throw new IOException("파일 저장 디렉토리 생성 실패: " + uploadDirectory.getAbsolutePath());
            }
        }

        // ✅ 파일명 설정 (현재시간_원본파일명)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = uploadDirectory + File.separator + fileName;

        // ✅ 파일 저장
        File destFile = new File(filePath);
        file.transferTo(destFile);

        // ✅ 파일 저장 경로 출력 (디버깅용)
        System.out.println("파일 저장 경로: " + destFile.getAbsolutePath());

        return destFile.getAbsolutePath(); // 저장된 파일 경로 반환
    }


    // OCR 텍스트에서 상품명만 파싱 (수량과 금액은 무시)

    // 기존 parseProductNames 메서드를 아래와 같이 수정하세요.
    private List<String> parseProductNames(String ocrResponseJson) {
        List<String> productNames = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(ocrResponseJson);
            JsonNode images = root.path("images");
            if (images.isArray()) {
                for (JsonNode image : images) {
                    JsonNode receipt = image.path("receipt");
                    JsonNode result = receipt.path("result");
                    JsonNode subResults = result.path("subResults");
                    if (subResults.isArray()) {
                        for (JsonNode subResult : subResults) {
                            JsonNode items = subResult.path("items");
                            if (items.isArray()) {
                                for (JsonNode item : items) {
                                    String name = item.path("name").path("text").asText(null);
                                    if (name != null && !name.isEmpty()) {
                                        productNames.add(name);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("파싱된 상품명 리스트: " + productNames);
        return productNames;
    }


    // /predict API 호출하여 상품명 기반 예측 카테고리 반환
    private String predictCategory(String productName) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String predictUrl = "http://localhost:8080/predict"; // 필요에 따라 변경
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 형식에 맞게 "text" 키로 상품명을 전달
            Map<String, List<String>> requestBody = new HashMap<>();
            requestBody.put("text", Collections.singletonList(productName));

            HttpEntity<Map<String, List<String>>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(predictUrl, entity, Map.class);

            System.out.println("🔹 /predict API 응답: " + response.getBody());

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // 기존 방식: "category" 키가 있는 경우
                if (response.getBody().containsKey("category")) {
                    return response.getBody().get("category").toString();
                }
                // 새로운 방식: "predictions" 키가 있는 경우
                else if (response.getBody().containsKey("predictions")) {
                    Object predictionsObj = response.getBody().get("predictions");
                    if (predictionsObj instanceof Map) {
                        Map predictions = (Map) predictionsObj;
                        // 직접 상품명 키가 존재하는지 확인
                        if (predictions.containsKey(productName)) {
                            return predictions.get(productName).toString();
                        } else {
                            // 만약 정확한 키가 없으면, 유사한 키를 찾아 반환할 수 있습니다.
                            for (Object key : predictions.keySet()) {
                                if (key.toString().contains(productName) || productName.contains(key.toString())) {
                                    return predictions.get(key).toString();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "미분류";
    }
}
