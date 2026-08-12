package com.loyalty.rewards.reward.controller;

import com.loyalty.rewards.reward.dto.login.LoginResponse;
import com.loyalty.rewards.reward.dto.refresh.RefreshRequest;
import com.loyalty.rewards.reward.service.TokenRefreshService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/refresh")
public class TokenRefreshController {

    private final TokenRefreshService tokenRefreshService;

    @PostMapping
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshRequest refreshToken){
        return ResponseEntity.ok(tokenRefreshService.refresh(refreshToken));
    }
}
