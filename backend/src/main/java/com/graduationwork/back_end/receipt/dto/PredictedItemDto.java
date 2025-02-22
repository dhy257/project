package com.graduationwork.back_end.receipt.dto;

public class PredictedItemDto {
    private Long receiptId;
    private String productName;
    private String category;

    public PredictedItemDto() {
    }

    public PredictedItemDto(Long receiptId, String productName, String category) {
        this.receiptId = receiptId;
        this.productName = productName;
        this.category = category;
    }

    public Long getReceiptId() {
        return receiptId;
    }
    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
