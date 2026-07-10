package com.loyalty.rewards.reward.controller;

import com.loyalty.rewards.reward.dto.RewardRequest;
import com.loyalty.rewards.reward.dto.RewardResponse;
import com.loyalty.rewards.reward.service.RewardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @PostMapping
    public ResponseEntity<RewardResponse> createReward(@Valid @RequestBody RewardRequest rewardDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rewardService.createReward(rewardDto));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<RewardResponse>> getCustomerRewards(@PathVariable String customerId){
        return ResponseEntity.ok(rewardService.getCustomerRewards(customerId));
    }
}
