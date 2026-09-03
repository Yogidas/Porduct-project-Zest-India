package com.Zest.product_assesment.Service;

import com.Zest.product_assesment.Entity.RefreshToken;
import com.Zest.product_assesment.Entity.User;
import com.Zest.product_assesment.Exception.BadRequestException;
import com.Zest.product_assesment.Exception.ResourceNotFoundException;
import com.Zest.product_assesment.Exception.TokenRefreshException;
import com.Zest.product_assesment.Repositories.RefreshTokenRepository;
import com.Zest.product_assesment.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${spring.security.jwt.refresh-ttl-seconds:604800}")
    private long refreshTtlSeconds;

    @Transactional
    public RefreshToken getOrCreateRefreshToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        // Check if an active refresh token already exists for this user
        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUser(user);

        if (existingTokenOpt.isPresent()) {
            RefreshToken existingToken = existingTokenOpt.get();

            // If the token is still valid (not expired), return it directly
            if (existingToken.getExpiryDate().compareTo(Instant.now()) > 0) {
                return existingToken;
            } else {
                // If it is expired, delete it so we can issue a fresh one
                refreshTokenRepository.delete(existingToken);
                refreshTokenRepository.flush();
            }
        }

        // Create a new refresh token if none existed or if the previous one expired
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(refreshTtlSeconds))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please make a new login request.");
        }
        return token;
    }
}