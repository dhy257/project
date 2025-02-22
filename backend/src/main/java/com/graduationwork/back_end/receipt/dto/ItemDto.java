package com.graduationwork.back_end.receipt.dto;

import com.graduationwork.back_end.receipt.model.Item;  // 🔥 추가
import com.graduationwork.back_end.receipt.model.ItemStatus; // 🔥 ItemStatus도 필요할 경우 추가


public class ItemDto {
    private String name;
    private String category;
    private String expirationDate;
    private String status;

    // 🔥 기존 생성자 (여전히 필요할 수 있음)
    public ItemDto(String name, String category, String expirationDate, String status) {
        this.name = name;
        this.category = category;
        this.expirationDate = expirationDate;
        this.status = status;
    }

    // 🔥 새로 추가할 생성자 (Item 객체를 직접 받을 수 있도록)
    public ItemDto(Item item) {
        this.name = item.getName();
        this.category = item.getCategory();
        this.expirationDate = item.getExpirationDate() != null ? item.getExpirationDate().toString() : null;
        this.status = item.getStatus().toString();
    }

    // ✅ Getter 추가 (JSON 직렬화를 위해 필요할 수 있음)
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getStatus() {
        return status;
    }
}
