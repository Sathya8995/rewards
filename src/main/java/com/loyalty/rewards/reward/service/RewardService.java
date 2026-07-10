package com.loyalty.rewards.reward.service;

import com.loyalty.rewards.reward.dto.RewardRequest;
import com.loyalty.rewards.reward.dto.RewardResponse;
import com.loyalty.rewards.reward.entity.Reward;
import com.loyalty.rewards.reward.entity.RewardStatus;
import com.loyalty.rewards.reward.repository.RewardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RewardService {

    private final RewardRepository repository;

    public RewardResponse createReward(RewardRequest rewardDto) {
        Reward reward = new Reward();
        reward.setCustomerId(rewardDto.getCustomerId());
        reward.setRewardType(rewardDto.getRewardType());
        reward.setPoints(rewardDto.getPoints());
        reward.setExpiresAt(rewardDto.getExpiresAt());
        reward.setIssuedAt(LocalDateTime.now());
        reward.setStatus(RewardStatus.ISSUED);
        reward.setRedeemedAt(null);
        repository.save(reward);
        return mapToDto(reward);
    }

    public RewardResponse mapToDto(Reward reward){
            RewardResponse rewardResponse = new RewardResponse();
            rewardResponse.setRewardType(reward.getRewardType());
            rewardResponse.setPoints(reward.getPoints());
            rewardResponse.setId(reward.getId());
            rewardResponse.setExpiresAt(reward.getExpiresAt());
            rewardResponse.setStatus(reward.getStatus());
            rewardResponse.setCustomerId(reward.getCustomerId());
            rewardResponse.setIssuedAt(reward.getIssuedAt());
            rewardResponse.setRedeemedAt(reward.getRedeemedAt());
            rewardResponse.setCreatedAt(reward.getCreatedAt());
            rewardResponse.setUpdatedAt(reward.getUpdatedAt());

            return rewardResponse;


    }

    public List<RewardResponse> getCustomerRewards(String customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toUnmodifiableList());


    }
}
