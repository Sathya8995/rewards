package com.loyalty.rewards.reward.dto.auth;

import com.loyalty.rewards.reward.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Response containing user details")
public record RegisterResponse(
        @Schema(
                description = "Unique user ID",
                example = "1"
        )
        Long id,
        @Schema(
                description = "Username to registe"
        )
        String username,
        Role role,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}