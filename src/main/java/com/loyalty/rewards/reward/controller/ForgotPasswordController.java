package com.loyalty.rewards.reward.controller;

import com.loyalty.rewards.reward.dto.forgotpassword.ForgotPasswordRequest;
import com.loyalty.rewards.reward.dto.forgotpassword.ForgotPasswordResponse;
import com.loyalty.rewards.reward.service.ForgotPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/forgot-password")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest
            ){

        return ResponseEntity.ok(forgotPasswordService.forgotPassword(forgotPasswordRequest));

    }
}
