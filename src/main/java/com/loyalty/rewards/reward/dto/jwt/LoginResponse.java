package com.loyalty.rewards.reward.dto.jwt;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Long expiresIn
) {
}
