package com.loyalty.rewards.reward.controller;

import com.loyalty.rewards.reward.dto.RewardRequest;
import com.loyalty.rewards.reward.dto.RewardResponse;
import com.loyalty.rewards.reward.entity.Reward;
import com.loyalty.rewards.reward.service.RewardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@AllArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @PostMapping
    public ResponseEntity<RewardResponse> createReward(@RequestBody RewardRequest rewardDto){
        return ResponseEntity.ok(rewardService.createReward(rewardDto));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<RewardResponse>> getCustomerRewards(@PathVariable String customerId){
        return ResponseEntity.ok(rewardService.getCustomerRewards(customerId));
    }
}
