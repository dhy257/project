package com.graduationwork.back_end.receipt.controller;

import com.graduationwork.back_end.receipt.dto.PredictedItemDto;
import com.graduationwork.back_end.receipt.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    /**
     * 영수증 업로드 → OCR 처리, Receipt DB 저장, 상품명 파싱 및 예측
     * 예측된 상품명+카테고리 리스트를 반환
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadReceipt(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId) {
        try {
            List<PredictedItemDto> predictedItems = receiptService.processAndPredict(file, userId);
            return ResponseEntity.ok(predictedItems);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
