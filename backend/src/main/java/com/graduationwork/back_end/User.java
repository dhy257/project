package com.graduationwork.back_end;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")  // DB 컬럼명
    private Long userId;         // 자바 필드명

    private String username;
    private String email;
    private String password;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.created_at = LocalDateTime.now();
    }

    // 수정된 getter와 setter
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @PrePersist
    public void prePersist() {
        if (created_at == null) {
            created_at = LocalDateTime.now(); // 현재 시간으로 설정
        }
    }
}
