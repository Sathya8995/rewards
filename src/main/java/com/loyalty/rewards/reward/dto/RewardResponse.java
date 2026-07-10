package com.loyalty.rewards.reward.dto;

import com.loyalty.rewards.reward.entity.RewardStatus;
import com.loyalty.rewards.reward.entity.RewardType;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class RewardResponse {


    private Long id;

    private String customerId;

    private RewardType rewardType;

    private Integer points;

    private RewardStatus status;

    private LocalDateTime issuedAt;

    private LocalDateTime expiresAt;

    private LocalDateTime redeemedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
