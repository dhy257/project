package com.graduationwork.back_end.receipt.dto;

import java.util.ArrayList;
import java.util.List;

public class FinalizeItemsRequest {
    private Long receiptId;
    private List<ItemRequestDto> items = new ArrayList<>(); // ✅ 기본값으로 빈 리스트 설정

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public List<ItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<ItemRequestDto> items) {
        this.items = (items != null) ? items : new ArrayList<>(); // ✅ null 방지
    }
}
