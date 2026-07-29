package com.loyalty.rewards.reward.dto;
import com.loyalty.rewards.reward.entity.RewardStatus;
import com.loyalty.rewards.reward.entity.RewardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Response containing reward details")
public class RewardResponse {

    @Schema(
            description = "Unique reward ID",
            example = "1"
    )
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

    @Schema(
            description = "Current reward status",
            example = "ISSUED"
    )
    private RewardStatus status;

    @Schema(
            description = "Date and time when the reward was issued",
            example = "2026-07-29T12:00:00"
    )
    private LocalDateTime issuedAt;

    @Schema(
            description = "Reward expiration date and time",
            example = "2026-08-30T12:00:00"
    )
    private LocalDateTime expiresAt;

    @Schema(
            description = "Date and time when the reward was redeemed",
            example = "2026-07-30T10:30:00",
            nullable = true
    )
    private LocalDateTime redeemedAt;

    @Schema(
            description = "Date and time when the reward record was created",
            example = "2026-07-29T12:00:00"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Date and time when the reward record was last updated",
            example = "2026-07-30T10:30:00"
    )
    private LocalDateTime updatedAt;
}