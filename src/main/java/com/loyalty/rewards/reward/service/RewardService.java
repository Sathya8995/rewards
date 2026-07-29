package com.loyalty.rewards.reward.service;

import com.loyalty.rewards.reward.dto.RewardRequest;
import com.loyalty.rewards.reward.dto.RewardResponse;
import com.loyalty.rewards.reward.entity.Reward;
import com.loyalty.rewards.reward.entity.RewardStatus;
import com.loyalty.rewards.reward.exception.RewardNotFoundException;
import com.loyalty.rewards.reward.exception.RewardRedemptionException;
import com.loyalty.rewards.reward.repository.RewardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;

    public RewardResponse createReward(RewardRequest rewardDto) {
        Reward reward = new Reward();
        log.debug("Creating reward for customerId= {},rewardType= {}, points= {}",
                rewardDto.getCustomerId(),
                rewardDto.getRewardType(),
                rewardDto.getPoints());
        reward.setCustomerId(rewardDto.getCustomerId());
        reward.setRewardType(rewardDto.getRewardType());
        reward.setPoints(rewardDto.getPoints());
        reward.setExpiresAt(rewardDto.getExpiresAt());
        reward.setIssuedAt(LocalDateTime.now());
        reward.setStatus(RewardStatus.ISSUED);
        Reward savedReward = rewardRepository.save(reward);
        log.info("Reward created successfully, rewardId={}, customerId={}",
                savedReward.getId(),
                savedReward.getCustomerId());
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
                .orElseThrow(() -> {
                    log.warn("Reward not found for reward Id: {}", rewardId);
                    return new RewardNotFoundException(rewardId);});

    }

    @Transactional
    public RewardResponse redeemReward(Long rewardId) {

        LocalDateTime now = LocalDateTime.now();

        log.debug("Attempting to redeem rewardId={}", rewardId);
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> {
                    log.warn(
                            "Reward not found during redemption, rewardId={}",
                            rewardId
                    );

                    return new RewardNotFoundException(rewardId);
                });

        validateRewardForRedemption(reward, now);

        reward.setStatus(RewardStatus.REDEEMED);
        reward.setRedeemedAt(now);

        log.info(
                "Reward redeemed successfully, rewardId={}, customerId={}",
                reward.getId(),
                reward.getCustomerId()
        );
        return mapToDto(reward);

    }

    private void validateRewardForRedemption(Reward reward, LocalDateTime now) {
        if(reward.getStatus() != RewardStatus.ISSUED){
            log.warn(
                    "Reward redemption rejected: Reward's current status is not (ISSUED), rewardId={}, status={}",
                    reward.getId(),
                    reward.getStatus()
            );
            throw new RewardRedemptionException( "Reward cannot be redeemed from status: " + reward.getStatus());
        }

        if (!reward.getExpiresAt().isAfter(now)) {
            log.warn(
                    "Reward redemption rejected: Reward has expired, rewardId={}, status={}",
                    reward.getId(),
                    reward.getStatus()
            );
            throw new RewardRedemptionException( "Reward has expired");
        }
    }
}
