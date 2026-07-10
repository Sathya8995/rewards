package com.loyalty.rewards.reward.exception;

public class RewardNotFoundException extends RuntimeException {
    public RewardNotFoundException(Long rewardId) {
        super("Reward not found with Reward id: " + rewardId);
    }

    public RewardNotFoundException(String customerId) {
        super("Reward not found with Customer id: " + customerId);
    }
}
