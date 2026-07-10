package com.loyalty.rewards.reward.exception;

public class RewardNotFoundException extends RuntimeException {
    public RewardNotFoundException(Long rewardId) {
        super("Reward not found with Reward id: " + rewardId);
    }
}
