package com.loyalty.rewards.reward.dto;

import com.loyalty.rewards.reward.entity.RewardType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RewardRequest {
    @NotBlank(message = "Customer Id cannot be null")
    private String customerId;

    @NotNull(message = "Reward Type cannot be null")
    private RewardType rewardType;

    @NotNull(message = "Points cannot be null")
    @Min(1)
    private Integer points;

    private LocalDateTime expiresAt;
}
