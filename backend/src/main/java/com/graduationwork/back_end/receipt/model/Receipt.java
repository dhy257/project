package com.graduationwork.back_end.receipt.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import com.graduationwork.back_end.User;

@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // User 테이블과 연결
    @JoinColumn(name = "user_id", nullable = false)  // FK 설정
    private User user;

    @Lob
    private String receiptImage;  // 파일 경로나 Base64

    @Lob
    private String receiptText;   // OCR 전체 텍스트

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    // Getters and Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getReceiptImage() {
        return receiptImage;
    }
    public void setReceiptImage(String receiptImage) {
        this.receiptImage = receiptImage;
    }

    public String getReceiptText() {
        return receiptText;
    }
    public void setReceiptText(String receiptText) {
        this.receiptText = receiptText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
}
