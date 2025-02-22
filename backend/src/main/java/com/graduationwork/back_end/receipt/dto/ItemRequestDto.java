package com.graduationwork.back_end.receipt.dto;

public class ItemRequestDto {
    private String productName;
    private String category;
    private String expirationDate; // "yyyy-MM-dd" 형식 또는 null

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
    public String getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
