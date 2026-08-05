package com.loyalty.rewards.reward.controller;

import com.loyalty.rewards.reward.dto.RewardRequest;
import com.loyalty.rewards.reward.dto.RewardResponse;
import com.loyalty.rewards.reward.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
@Tag(
        name = "Rewards",
        description = "APIs for creating, retrieving, and redeeming rewards"
)
public class RewardController {

    private final RewardService rewardService;

    @Operation(
            summary = "Create a reward",
            description = "Creates a new reward for a customer with ISSUED status"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Reward created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid reward request"
            )
    })
    @PostMapping
    public ResponseEntity<RewardResponse> createReward(@Valid @RequestBody RewardRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rewardService.createReward(request));
    }

    @Operation(
            summary = "Get customer rewards",
            description = "Returns all rewards for a customer ordered by issue date descending"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer rewards retrieved successfully"
            )
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<RewardResponse>> getCustomerRewards(@Parameter(
            description = "Unique customer ID",
            example = "CUST1001",
            required = true
    )@PathVariable String customerId){
        return ResponseEntity.ok(rewardService.getCustomerRewards(customerId));
    }

    @Operation(
            summary = "Get reward by ID",
            description = "Returns a reward using its unique reward ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reward found successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reward not found"
            )
    })
    @GetMapping("/{rewardId}")
    public ResponseEntity<RewardResponse> getRewardById(@PathVariable Long rewardId){
        return ResponseEntity.ok(rewardService.getRewardById(rewardId));
    }

    @Operation(
            summary = "Redeem a reward",
            description = "Redeems an eligible, unexpired reward"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reward redeemed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Reward cannot be redeemed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reward not found"
            )
    })
    @PatchMapping("/{rewardId}/redeem")
    public ResponseEntity<RewardResponse> redeemReward(@Parameter(
            description = "Unique reward ID",
            example = "1",
            required = true
    )@PathVariable Long rewardId){
        return ResponseEntity.ok(rewardService.redeemReward(rewardId));
    }

    @GetMapping("/current-user")
    public Map<String, Object> getCurrentUser(Authentication authentication) {

        return Map.of(
                "username", authentication.getName(),
                "authenticated", authentication.isAuthenticated(),
                "authorities", authentication.getAuthorities()
        );
    }
}
