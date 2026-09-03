package com.Zest.product_assesment.Security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Value("${spring.security.jwt.access-token-cookie-name:access_token}")
    private String accessTokenCookieName;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        log.info("Request URI : {}", request.getRequestURI());

        // 1. Extract token from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (accessTokenCookieName.equals(cookie.getName()) || "access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 2. Fallback to Authorization header if not found in cookies
        if (token == null || token.isBlank()) {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
            }
        }

        log.info("Token present : {}", token != null && !token.isBlank());

        // 3. Process token and authenticate user if found
        if (token != null && !token.isBlank()) {
            try {
                if (jwtService.isAccessToken(token)) {
                    String userIdentifier = jwtService.getUserEmail(token);

                    log.info("User : {}", userIdentifier);

                    if (userIdentifier != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userIdentifier);

                        if (userDetails != null && userDetails.isEnabled()) {
                            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            SecurityContextHolder.getContext().setAuthentication(auth);

                            log.info("Authorities : {}", auth.getAuthorities());
                            log.info("Authenticated : {}", auth.isAuthenticated());
                            log.info("Authentication Success");
                        }
                        else {
                            log.info("Authentication skipped - userDetails null or disabled : {}", userDetails);
                        }

                    }
                }
            } catch (JwtException | UsernameNotFoundException e) {
                // Clear context if token is invalid or user doesn't exist
                logger.info("This is catch block :{} " ,e);
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}