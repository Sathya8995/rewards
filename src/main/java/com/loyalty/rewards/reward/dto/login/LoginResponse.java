package com.loyalty.rewards.reward.dto.login;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn
) {
}
