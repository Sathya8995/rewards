package com.loyalty.rewards.reward.service;

import com.loyalty.rewards.reward.dto.RewardResponse;
import com.loyalty.rewards.reward.entity.Reward;
import com.loyalty.rewards.reward.entity.RewardStatus;
import com.loyalty.rewards.reward.entity.RewardType;
import com.loyalty.rewards.reward.exception.RewardNotFoundException;
import com.loyalty.rewards.reward.exception.RewardRedemptionException;
import com.loyalty.rewards.reward.repository.RewardRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RewardServiceIntegrationTest {

    @Autowired
    private RewardService rewardService;

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldRedeemRewardSuccessfully() {
        Reward reward = new Reward();
        LocalDateTime now = LocalDateTime.now();

        //Arrange
        reward.setCustomerId("MCUST1000");
        reward.setRewardType(RewardType.GROCERY);
        reward.setPoints(100);
        reward.setExpiresAt(now.plusDays(30));
        reward.setIssuedAt(now);
        reward.setStatus(RewardStatus.ISSUED);
        Reward savedReward = rewardRepository.save(reward);

        //Act
        entityManager.flush();
        entityManager.clear();

        RewardResponse rewardResponse = rewardService.redeemReward(savedReward.getId());

        entityManager.flush();
        entityManager.clear();

        Reward updatedReward = rewardRepository.findById(savedReward.getId()).orElseThrow();

        //Assert
        assertEquals(RewardStatus.REDEEMED, updatedReward.getStatus());
        assertEquals(RewardStatus.REDEEMED, rewardResponse.getStatus());
        assertNotNull(updatedReward.getRedeemedAt());
        assertNotNull(rewardResponse.getRedeemedAt());

    }

    @Test
    void shouldThrowRewardNotFoundExceptionWhenRewardDoesNotExist(){

        //Arrange
        Long nonExistingRewardId = Long.MAX_VALUE;

        //Act and Assert
        RewardNotFoundException exception =
                assertThrows(
                        RewardNotFoundException.class,
                        () -> rewardService.redeemReward(nonExistingRewardId)
                );

        assertEquals(
                "Reward not found with Reward id: " + nonExistingRewardId,
                exception.getMessage()
        );

    }

    @Test
    void shouldThrowRewardRedemptionExceptionWhenRewardIsAlreadyRedeemed(){

        Reward reward = new Reward();
        LocalDateTime now = LocalDateTime.now();

        //Arrange
        reward.setCustomerId("MCUST1000");
        reward.setRewardType(RewardType.GROCERY);
        reward.setPoints(100);
        reward.setExpiresAt(now.plusDays(30));
        reward.setIssuedAt(now);
        reward.setStatus(RewardStatus.REDEEMED);
        reward.setRedeemedAt(now);
        Reward savedReward = rewardRepository.save(reward);

        //Act and Assert
        entityManager.flush();
        entityManager.clear();

        RewardRedemptionException exception = assertThrows(
                RewardRedemptionException.class,
                () -> rewardService.redeemReward(savedReward.getId()));

        assertEquals("Reward cannot be redeemed from status: REDEEMED", exception.getMessage());

        entityManager.flush();
        entityManager.clear();

        Reward updatedReward = rewardRepository
                .findById(savedReward.getId())
                .orElseThrow();

        assertEquals(RewardStatus.REDEEMED, updatedReward.getStatus());
        assertNotNull(updatedReward.getRedeemedAt());
    }

    @Test
    void shouldThrowRewardRedemptionExceptionWhenRewardHasExpired(){
        Reward reward = new Reward();
        LocalDateTime now = LocalDateTime.now();

        //Arrange
        reward.setCustomerId("MCUST1000");
        reward.setRewardType(RewardType.GROCERY);
        reward.setPoints(100);
        reward.setExpiresAt(now.minusDays(1));
        reward.setIssuedAt(now.minusDays(31));
        reward.setStatus(RewardStatus.ISSUED);
        Reward savedReward = rewardRepository.save(reward);

        //Act and Assert
        entityManager.flush();
        entityManager.clear();

        RewardRedemptionException exception = assertThrows(
                RewardRedemptionException.class,
                () -> rewardService.redeemReward(savedReward.getId()));

        assertEquals("Reward has expired", exception.getMessage());

        entityManager.flush();
        entityManager.clear();

        Reward updatedReward = rewardRepository
                .findById(savedReward.getId())
                .orElseThrow();

        assertNull(updatedReward.getRedeemedAt());
    }
}