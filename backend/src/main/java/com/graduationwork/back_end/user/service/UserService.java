package com.graduationwork.back_end.user.service;

import com.graduationwork.back_end.user.User;
import com.graduationwork.back_end.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // 회원가입
    public void registerUser(String username, String email, String password) {

        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword); // 암호화된 비밀번호

        // created_at ???

        // DB에 저장
        userRepository.save(user);
    }

    // 로그인
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user를 찾을 수 없음"));

        // 암호화된 비밀번호와 비교
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("유효하지 않은 비밀번호");
        }

        return user;  // -> 로그인 성공
    }

}
