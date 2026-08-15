package com.loyalty.rewards.reward.dto.forgotpassword;

import java.time.LocalDateTime;

public record ForgotPasswordResponse(
        String username,
        String resetToken,
        LocalDateTime expiration
) {
}
