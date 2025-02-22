package com.graduationwork.back_end.receipt.dto;

public class UpdateItemStatusRequest {
    private Long itemId;
    private String status; // 예: "보관중", "사용", "버림"

    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
