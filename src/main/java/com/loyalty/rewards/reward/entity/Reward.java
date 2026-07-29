package com.loyalty.rewards.reward.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rewards",
        indexes = {
                @Index(
                        name = "idx_rewards_customer_id",
                        columnList = "customer_id"
                )
        })
@Getter
@Setter
@NoArgsConstructor
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", length = 100, nullable = false)
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type", length = 50, nullable = false)
    private RewardType rewardType;

    @Column(nullable = false)
    private Integer points;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RewardStatus status;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "redeemed_at")
    private LocalDateTime redeemedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
