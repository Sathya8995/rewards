package com.loyalty.rewards.reward.controller;

import com.loyalty.rewards.reward.dto.changepassword.ChangePasswordRequest;
import com.loyalty.rewards.reward.service.ChangePasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/change-password")
public class ChangePasswordController {

    private final ChangePasswordService changePasswordService;

    @PostMapping
    public ResponseEntity<Void> changePassword
            (@Valid @RequestBody ChangePasswordRequest changePasswordRequest){

        changePasswordService.changePassword(changePasswordRequest);
        return ResponseEntity.noContent().build();
    }
}
