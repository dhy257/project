package com.graduationwork.back_end.receipt.dto;

import java.util.ArrayList;
import java.util.List;

public class ManualAddItemsRequest {
    private Long userId;
    private List<ItemRequestDto> items = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public List<ItemRequestDto> getItems() {
        return items;
    }
    public void setItems(List<ItemRequestDto> items) {
        this.items = (items != null) ? items : new ArrayList<>();
    }
}
