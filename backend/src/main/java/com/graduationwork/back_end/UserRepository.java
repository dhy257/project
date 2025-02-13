package com.graduationwork.back_end;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // email으로 유저를 찾음
    Optional<User> findByEmail(String email);
}
