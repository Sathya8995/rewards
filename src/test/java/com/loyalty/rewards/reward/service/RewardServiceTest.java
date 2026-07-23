package com.loyalty.rewards.reward.service;
import com.loyalty.rewards.reward.dto.RewardResponse;
import com.loyalty.rewards.reward.entity.Reward;
import com.loyalty.rewards.reward.entity.RewardStatus;
import com.loyalty.rewards.reward.entity.RewardType;
import com.loyalty.rewards.reward.exception.RewardNotFoundException;
import com.loyalty.rewards.reward.exception.RewardRedemptionException;
import com.loyalty.rewards.reward.repository.RewardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {
    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private RewardService rewardService;

    @Test
    void shouldRedeemIssuedRewardSuccessfully() {

        Reward reward = new Reward();
        LocalDateTime now = LocalDateTime.now();
        reward.setId(1L);
        reward.setCustomerId("MCUST1000");
        reward.setRewardType(RewardType.GROCERY);
        reward.setPoints(100);
        reward.setExpiresAt(now.plusDays(30));
        reward.setIssuedAt(now);
        reward.setStatus(RewardStatus.ISSUED);

        when(rewardRepository.findById(1L)).thenReturn(Optional.of(reward));

        RewardResponse rewardResponse = rewardService.redeemReward(1L);

        assertEquals(RewardStatus.REDEEMED, reward.getStatus());
        assertEquals(RewardStatus.REDEEMED, rewardResponse.getStatus());
        assertNotNull(reward.getRedeemedAt());
        assertNotNull(rewardResponse.getRedeemedAt());

        verify(rewardRepository).findById(1L);

    }

    @Test
    void shouldThrowRewardNotFoundExceptionSuccessfully() {

        when(rewardRepository.findById(1L))
                .thenReturn(Optional.empty());

        RewardNotFoundException exception =
                assertThrows(
                        RewardNotFoundException.class,
                        () -> rewardService.redeemReward(1L)
                );

        assertEquals(
                "Reward not found with Reward id: 1",
                exception.getMessage()
        );

        verify(rewardRepository).findById(1L);
    }

    @Test
    void shouldThrowRewardRedemptionExceptionWhenRewardIsAlreadyRedeemed(){

        Reward reward = new Reward();
        LocalDateTime now = LocalDateTime.now();
        reward.setId(1L);
        reward.setCustomerId("MCUST1000");
        reward.setRewardType(RewardType.GROCERY);
        reward.setPoints(100);
        reward.setExpiresAt(now.plusDays(30));
        reward.setIssuedAt(now);
        reward.setStatus(RewardStatus.REDEEMED);

        when(rewardRepository.findById(1L)).thenReturn(Optional.of(reward));

        RewardRedemptionException exception = assertThrows(
                RewardRedemptionException.class,
                () -> rewardService.redeemReward(1L));

        assertEquals("Reward cannot be redeemed from status: REDEEMED", exception.getMessage());

        verify(rewardRepository).findById(1L);
    }

    @Test
    void shouldThrowRewardRedemptionExceptionWhenRewardHasExpired(){

        Reward reward = new Reward();
        LocalDateTime now = LocalDateTime.now();
        reward.setId(1L);
        reward.setCustomerId("MCUST1000");
        reward.setRewardType(RewardType.GROCERY);
        reward.setPoints(100);
        reward.setExpiresAt(now.minusDays(2));
        reward.setIssuedAt(now);
        reward.setStatus(RewardStatus.ISSUED);

        when(rewardRepository.findById(1L)).thenReturn(Optional.of(reward));

        RewardRedemptionException exception = assertThrows(
                RewardRedemptionException.class,
                () -> rewardService.redeemReward(1L));

        assertEquals("Reward has expired", exception.getMessage());

        verify(rewardRepository).findById(1L);
    }
}
