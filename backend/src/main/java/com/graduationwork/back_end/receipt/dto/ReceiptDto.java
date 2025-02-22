package com.graduationwork.back_end.receipt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.graduationwork.back_end.receipt.model.Receipt;

import java.time.LocalDateTime;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptDto {
    private Long id;
    private String receiptImage;
    private String receiptText;
    private LocalDateTime createdAt;

    public ReceiptDto(Receipt receipt) {
        this.id = receipt.getId();
        this.receiptImage = receipt.getReceiptImage();
        this.receiptText = receipt.getReceiptText();
        this.createdAt = receipt.getCreatedAt();
    }

    // Getter & Setter
}
