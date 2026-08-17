package com.loyalty.rewards.reward.dto.forgotpassword;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank
        String username
) {
}
