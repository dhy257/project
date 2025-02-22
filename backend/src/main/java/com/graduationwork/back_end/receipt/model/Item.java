package com.graduationwork.back_end.receipt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Receipt와 다대일 관계 (외래키: receipt_id)
    @ManyToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    @JsonIgnore
    private Receipt receipt;

    @Column(nullable = false)
    private String name;  // 상품명

    @Column(nullable = false)
    private String category;  // 카테고리

    private LocalDate expirationDate;  // 유통기한

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus status = ItemStatus.보관중;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Receipt getReceipt() {
        return receipt;
    }
    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
    public ItemStatus getStatus() {
        return status;
    }
    public void setStatus(ItemStatus status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
