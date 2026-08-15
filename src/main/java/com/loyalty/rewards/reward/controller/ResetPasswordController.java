package com.loyalty.rewards.reward.controller;

import com.loyalty.rewards.reward.dto.resetpassword.ResetPasswordRequest;
import com.loyalty.rewards.reward.service.ResetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/reset-password")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
        resetPasswordService.resetPassword(resetPasswordRequest);
        return ResponseEntity.noContent().build();
    }
}
