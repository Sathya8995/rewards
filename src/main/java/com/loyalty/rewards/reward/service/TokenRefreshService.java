package com.loyalty.rewards.reward.service;


import com.loyalty.rewards.reward.dto.login.LoginResponse;
import com.loyalty.rewards.reward.dto.refresh.RefreshRequest;
import com.loyalty.rewards.reward.exception.InvalidRefreshTokenException;
import com.loyalty.rewards.reward.security.CustomUserDetailsService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenRefreshService {

    private final JwtService jwtService;

    private final CustomUserDetailsService customUserDetailsService;

    public LoginResponse refresh(RefreshRequest refreshToken) {
        try {
            log.debug("Extracting token type");
            String tokenType = jwtService.extractTokenType(refreshToken.refreshToken());

            if (!"refresh".equals(tokenType)) {
                throw new InvalidRefreshTokenException(
                        "Token is not a refresh token"
                );
            }

            log.debug("Extracting username");
            String username = jwtService.extractUsername(refreshToken.refreshToken());

            log.debug("Fetching User Details from DataBase");
            UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(username);

            log.debug("Validating Refresh Token");
            if (!jwtService.isTokenValid(refreshToken.refreshToken(), userDetails)) {
                throw new InvalidRefreshTokenException(
                        "Refresh token is invalid"
                );
            }
            log.info("Validation successful");

            log.debug("Generating new Access Token");
            String newAccessToken =
                    jwtService.generateToken(userDetails);
            log.info("Access Token generation successful");

            return new LoginResponse(
                    newAccessToken,
                    refreshToken.refreshToken(),
                    "Bearer",
                    jwtService.getExpirationSeconds()
            );

        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidRefreshTokenException(
                    "Refresh token is invalid or expired"
            );
        }
    }

}
