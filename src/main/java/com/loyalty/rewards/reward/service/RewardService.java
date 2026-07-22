package com.loyalty.rewards.reward.service;

import com.loyalty.rewards.reward.dto.RewardRequest;
import com.loyalty.rewards.reward.dto.RewardResponse;
import com.loyalty.rewards.reward.entity.Reward;
import com.loyalty.rewards.reward.entity.RewardStatus;
import com.loyalty.rewards.reward.exception.RewardNotFoundException;
import com.loyalty.rewards.reward.exception.RewardRedemptionException;
import com.loyalty.rewards.reward.repository.RewardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;

    public RewardResponse createReward(RewardRequest rewardDto) {
        Reward reward = new Reward();
        reward.setCustomerId(rewardDto.getCustomerId());
        reward.setRewardType(rewardDto.getRewardType());
        reward.setPoints(rewardDto.getPoints());
        reward.setExpiresAt(rewardDto.getExpiresAt());
        reward.setIssuedAt(LocalDateTime.now());
        reward.setStatus(RewardStatus.ISSUED);
        Reward savedReward = rewardRepository.save(reward);
        return mapToDto(savedReward);
    }

    private RewardResponse mapToDto(Reward reward){
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
        List<Reward> rewards = rewardRepository.findByCustomerIdOrderByIssuedAtDesc(customerId);

        return rewards
                .stream()
                .map(this::mapToDto)
                .toList();


    }

    public RewardResponse getRewardById(Long rewardId) {
        return rewardRepository.findById(rewardId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RewardNotFoundException(rewardId));

    }

    @Transactional
    public RewardResponse redeemReward(Long rewardId) {

        LocalDateTime now = LocalDateTime.now();

        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new RewardNotFoundException(rewardId));

        if(reward.getStatus() != RewardStatus.ISSUED){
            throw new RewardRedemptionException( "Reward cannot be redeemed from status: " + reward.getStatus());
        }

        if (!(reward.getExpiresAt().isAfter(now))) {
            throw new RewardRedemptionException( "Reward already expired");
        }

            reward.setStatus(RewardStatus.REDEEMED);
            reward.setRedeemedAt(now);
            Reward savedReward = rewardRepository.save(reward);
            return mapToDto(savedReward);

    }
}
