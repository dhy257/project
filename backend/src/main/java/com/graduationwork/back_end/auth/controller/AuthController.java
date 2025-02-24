package com.graduationwork.back_end.auth.controller;

import com.graduationwork.back_end.user.User;
import com.graduationwork.back_end.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class AuthController {

    @Autowired
    private UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> signup(@RequestBody User user) {
        userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        userService.authenticate(user.getEmail(), user.getPassword());
        return ResponseEntity.ok("로그인 성공");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 기존 세션 가져오기 (없으면 null 반환)

        if (session != null) {
            session.invalidate(); // 세션 무효화 (로그아웃 처리)
            return ResponseEntity.ok("✅ 로그아웃 성공!");
        } else {
            return ResponseEntity.badRequest().body("❌ 현재 로그인된 세션이 없습니다.");
        }
    }
}
