package com.loyalty.rewards.reward.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.rewards.reward.dto.RewardRequest;
import com.loyalty.rewards.reward.dto.RewardResponse;
import com.loyalty.rewards.reward.entity.RewardStatus;
import com.loyalty.rewards.reward.entity.RewardType;
import com.loyalty.rewards.reward.exception.RewardNotFoundException;
import com.loyalty.rewards.reward.exception.RewardRedemptionException;
import com.loyalty.rewards.reward.service.RewardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardController.class)
public class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateRewardSuccessfully() throws Exception {

        //ARRANGE
        LocalDateTime now = LocalDateTime.now();
        RewardRequest rewardRequest = new RewardRequest();

        rewardRequest.setCustomerId("CUST9999");
        rewardRequest.setPoints(100);
        rewardRequest.setRewardType(RewardType.GROCERY);
        rewardRequest.setExpiresAt(now.plusDays(30));

        String json = objectMapper.writeValueAsString(rewardRequest);

        RewardResponse rewardResponse = getRewardResponse(rewardRequest, now);

        when(rewardService.createReward(any(RewardRequest.class))).thenReturn(rewardResponse);

        //ACT, ASSERT
        mockMvc.perform(post("/api/rewards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value("CUST9999"))
                .andExpect(jsonPath("$.points").value(100))
                .andExpect(jsonPath("$.rewardType").value("GROCERY"))
                .andExpect(jsonPath("$.status").value("ISSUED"));

        verify(rewardService).createReward(any(RewardRequest.class));

    }

    private static RewardResponse getRewardResponse(RewardRequest rewardRequest, LocalDateTime now) {
        RewardResponse rewardResponse = new RewardResponse();
        rewardResponse.setRewardType(rewardRequest.getRewardType());
        rewardResponse.setPoints(rewardRequest.getPoints());
        rewardResponse.setId(1L);
        rewardResponse.setExpiresAt(rewardRequest.getExpiresAt());
        rewardResponse.setStatus(RewardStatus.ISSUED);
        rewardResponse.setCustomerId(rewardRequest.getCustomerId());
        rewardResponse.setIssuedAt(now);
        rewardResponse.setRedeemedAt(null);
        rewardResponse.setCreatedAt(now);
        rewardResponse.setUpdatedAt(now);
        return rewardResponse;
    }

    @Test
    void shouldReturnBadRequestWhenCreateRewardRequestIsInvalid() throws Exception {

        //ARRANGE
        LocalDateTime now = LocalDateTime.now();
        RewardRequest rewardRequest = new RewardRequest();

        rewardRequest.setPoints(100);
        rewardRequest.setRewardType(RewardType.GROCERY);
        rewardRequest.setExpiresAt(now.plusDays(30));

        String json = objectMapper.writeValueAsString(rewardRequest);

        RewardResponse rewardResponse = getRewardResponse(rewardRequest, now);

        when(rewardService.createReward(any(RewardRequest.class))).thenReturn(rewardResponse);

        //ACT, ASSERT
        mockMvc.perform(post("/api/rewards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(rewardService,never()).createReward(any(RewardRequest.class));
    }

    @Test
    void shouldRedeemRewardSuccessfully() throws Exception {

        //ARRANGE
        LocalDateTime now = LocalDateTime.now();
        RewardResponse rewardResponse = new RewardResponse();
        rewardResponse.setId(1L);
        rewardResponse.setCustomerId("CUST9999");
        rewardResponse.setRewardType(RewardType.GROCERY);
        rewardResponse.setPoints(100);
        rewardResponse.setStatus(RewardStatus.REDEEMED);
        rewardResponse.setIssuedAt(now.minusDays(30));
        rewardResponse.setExpiresAt(now.plusDays(30));
        rewardResponse.setRedeemedAt(now);

        when(rewardService.redeemReward(1L)).thenReturn(rewardResponse);

        //ACT, ASSERT
        mockMvc.perform(patch("/api/rewards/1/redeem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REDEEMED"))
                .andExpect(jsonPath("$.redeemedAt").exists());

        verify(rewardService).redeemReward(1L);
    }

    @Test
    void shouldReturnNotFoundWhenRedeemingNonExistingReward() throws Exception {

        //ARRANGE
        Long rewardId = 999L;
        when(rewardService.redeemReward(rewardId)).thenThrow(new RewardNotFoundException(rewardId));

        //ACT, ASSERT
        mockMvc.perform(patch("/api/rewards/999/redeem"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Reward not found with Reward id: 999"));

        verify(rewardService).redeemReward(rewardId);
    }

    @Test
    void shouldReturnBadRequestWhenRewardCannotBeRedeemed() throws Exception {

        //ARRANGE
        Long rewardId = 1L;
        when(rewardService.redeemReward(rewardId)).thenThrow(new RewardRedemptionException("Reward cannot be redeemed from status: REDEEMED"));

        //ACT, ASSERT
        mockMvc.perform(patch("/api/rewards/1/redeem"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Reward cannot be redeemed from status: REDEEMED"));

        verify(rewardService).redeemReward(rewardId);
    }

}
