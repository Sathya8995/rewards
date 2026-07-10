package com.loyalty.rewards.reward.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {
}