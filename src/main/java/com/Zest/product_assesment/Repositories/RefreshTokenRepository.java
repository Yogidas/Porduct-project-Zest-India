package com.Zest.product_assesment.Repositories;

import com.Zest.product_assesment.Entity.RefreshToken;
import com.Zest.product_assesment.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    @Modifying
    void deleteByUser(User user);
}