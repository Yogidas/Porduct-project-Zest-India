package com.Zest.product_assesment.Service;

import com.Zest.product_assesment.Entity.RefreshToken;
import com.Zest.product_assesment.Entity.Role;
import com.Zest.product_assesment.Entity.User;
import com.Zest.product_assesment.Exception.BadRequestException;
import com.Zest.product_assesment.Exception.TokenRefreshException;
import com.Zest.product_assesment.Repositories.UserRepository;
import com.Zest.product_assesment.Security.JwtService;
import com.Zest.product_assesment.dto.AuthRequestDTO;
import com.Zest.product_assesment.dto.JwtResponseDTO;
import com.Zest.product_assesment.dto.RefreshTokenRequestDTO;
import com.Zest.product_assesment.dto.RegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public String registerUser(RegisterRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new BadRequestException("Error: Username is already taken!");
        }

        Role assignedRole = requestDTO.getRole() != null ? requestDTO.getRole() : Role.USER;

        User newUser = User.builder()
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .role(assignedRole)
                .build();

        userRepository.save(newUser);
        return assignedRole.name();
    }

    public JwtResponseDTO login(AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword())
        );

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateAccessToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.getOrCreateRefreshToken(userDetails.getUsername());

            return JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .build();
        } else {
            throw new BadRequestException("Authentication failed!");
        }
    }

    public JwtResponseDTO refreshToken(RefreshTokenRequestDTO requestDTO) {
        return refreshTokenService.findByToken(requestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetails userDetails = org.springframework.security.core.userdetails.User
                            .withUsername(user.getUsername())
                            .password(user.getPassword())
                            .roles(user.getRole().name())
                            .build();

                    String accessToken = jwtService.generateAccessToken(userDetails);
                    RefreshToken newRefreshToken = refreshTokenService.getOrCreateRefreshToken(user.getUsername());

                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(newRefreshToken.getToken())
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token is not in database!"));
    }
}