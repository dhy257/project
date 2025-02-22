package com.graduationwork.back_end.receipt.repository;

import com.graduationwork.back_end.receipt.model.Item;
import com.graduationwork.back_end.receipt.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // 특정 사용자의 영수증과 연관된 "보관중" 아이템 조회
    List<Item> findByStatusAndReceiptUserUserId(ItemStatus status, Long userId);
}
