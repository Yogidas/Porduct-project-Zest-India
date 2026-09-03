package com.Zest.product_assesment.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long accessTtl;
    private final long refreshTtl;
    private final String issuer;

    private final String accessTokenCookieName;
    private final String refreshTokenCookieName;
    private final boolean cookieSecure;
    private final boolean httpOnly;
    private final String sameSite;

    public JwtService(
            @Value("${spring.security.jwt.secret}") String secret,
            @Value("${spring.security.jwt.access-ttl-seconds}") long accessTtl,
            @Value("${spring.security.jwt.refresh-ttl-seconds}") long refreshTtl,
            @Value("${spring.security.jwt.issuer}") String issuer,
            @Value("${spring.security.jwt.access-token-cookie-name}") String accessTokenCookieName,
            @Value("${spring.security.jwt.refresh-token-cookie-name}") String refreshTokenCookieName,
            @Value("${spring.security.jwt.cookie-secure}") boolean cookieSecure,
            @Value("${spring.security.jwt.http-only}") boolean httpOnly,
            @Value("${spring.security.jwt.same-site}") String sameSite) {

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTtl = accessTtl;
        this.refreshTtl = refreshTtl;
        this.issuer = issuer;
        this.accessTokenCookieName = accessTokenCookieName;
        this.refreshTokenCookieName = refreshTokenCookieName;
        this.cookieSecure = cookieSecure;
        this.httpOnly = httpOnly;
        this.sameSite = sameSite;
    }

    public String generateAccessToken(UserDetails user) {
        Instant now = Instant.now();
        String role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .claims(Map.of("email", user.getUsername(), "role", role, "type", "access"))
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtl)))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(UserDetails user, String jti) {
        Instant now = Instant.now();
        String role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");

        return Jwts.builder()
                .id(jti)
                .subject(user.getUsername())
                .claims(Map.of("email", user.getUsername(), "role", role, "type", "refresh"))
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshTtl)))
                .signWith(secretKey)
                .compact();
    }

    public Cookie createAccessTokenCookie(String token) {
        return buildCookie(accessTokenCookieName, token, accessTtl);
    }

    public Cookie createRefreshTokenCookie(String token) {
        return buildCookie(refreshTokenCookieName, token, refreshTtl);
    }

    private Cookie buildCookie(String name, String value, long ttlSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge((int) ttlSeconds);

        try {
            cookie.setAttribute("SameSite", sameSite);
        } catch (UnsupportedOperationException ignored) {}

        return cookie;
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    public boolean isAccessToken(String token) {
        return "access".equals(parseToken(token).getPayload().get("type", String.class));
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(parseToken(token).getPayload().get("type", String.class));
    }

    public String getUserEmail(String token) {
        return parseToken(token).getPayload().getSubject();
    }
}