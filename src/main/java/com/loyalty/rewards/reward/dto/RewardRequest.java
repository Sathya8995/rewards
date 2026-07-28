package com.loyalty.rewards.reward.dto;

import com.loyalty.rewards.reward.entity.RewardType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Request payload for creating a reward")
public class RewardRequest {
    @Schema(
            description = "Customer receiving the reward",
            example = "CUST9999"
    )
    @NotBlank(message = "Customer Id cannot be null")
    private String customerId;

    @Schema(
            description = "Reward category",
            example = "GROCERY"
    )
    @NotNull(message = "Reward Type cannot be null")
    private RewardType rewardType;

    @Schema(
            description = "Number of reward points",
            example = "100",
            minimum = "1"
    )
    @NotNull(message = "Points cannot be null")
    @Min(1)
    private Integer points;

    @Schema(
            description = "Reward expiration date and time",
            example = "2026-08-30T12:00:00"
    )
    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private LocalDateTime expiresAt;
}
