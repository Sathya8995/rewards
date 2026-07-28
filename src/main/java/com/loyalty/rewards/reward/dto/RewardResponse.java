package com.loyalty.rewards.reward.dto;

import com.loyalty.rewards.reward.entity.RewardStatus;
import com.loyalty.rewards.reward.entity.RewardType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Schema(description = "Response for creating a reward")
public class RewardResponse {


    private Long id;

    @Schema(
            description = "Customer receiving the reward",
            example = "CUST9999"
    )
    private String customerId;

    @Schema(
            description = "Reward category",
            example = "GROCERY"
    )
    private RewardType rewardType;

    @Schema(
            description = "Number of reward points",
            example = "100",
            minimum = "1"
    )
    private Integer points;

    private RewardStatus status;

    private LocalDateTime issuedAt;

    @Schema(
            description = "Reward expiration date and time",
            example = "2026-08-30T12:00:00"
    )
    private LocalDateTime expiresAt;

    private LocalDateTime redeemedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
