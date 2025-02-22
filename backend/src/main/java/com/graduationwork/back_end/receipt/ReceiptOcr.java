package com.graduationwork.back_end.receipt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReceiptOcr {

    @Value("${naver.ocr.secretKey}")
    private String secretKey;

    @Value("${naver.ocr.apiUrl}")
    private String apiUrl;

    // ✅ 기존 MultipartFile 처리 메서드 (파일을 저장 후 OCR 호출하도록 변경)
    public String extractTextFromImage(MultipartFile file) throws IOException {
        // 🔥 파일을 먼저 저장한 후, 저장된 파일을 OCR에 넘기도록 변경
        File tempFile = File.createTempFile("receipt_", ".png");
        file.transferTo(tempFile);

        // 저장된 파일을 OCR에 넘김
        return extractTextFromImage(tempFile);
    }

    // ✅ 저장된 파일을 OCR 처리하는 메서드 추가
    public String extractTextFromImage(File file) throws IOException {
        System.out.println("네이버 OCR API URL: " + apiUrl);
        byte[] imageBytes = Files.readAllBytes(file.toPath()); // 🔥 파일을 읽어서 OCR 처리
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-OCR-SECRET", secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{"
                + "\"version\":\"V2\","
                + "\"requestId\":\"" + System.currentTimeMillis() + "\","
                + "\"timestamp\":" + System.currentTimeMillis() + ","
                + "\"images\":[{"
                + "\"format\":\"png\","
                + "\"data\":\"" + base64Image + "\","
                + "\"name\":\"receipt\""
                + "}]"
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}