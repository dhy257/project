package com.graduationwork.back_end.receipt.repository;

import com.graduationwork.back_end.receipt.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
