package com.loyalty.rewards.reward.repository;

import com.loyalty.rewards.reward.entity.Reward;
import com.loyalty.rewards.reward.entity.RewardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByCustomerId(String customerId);

    List<Reward> findByCustomerIdAndStatus(
            String customerId,
            RewardStatus status
    );

    List<Reward> findByStatus(RewardStatus status);
}
