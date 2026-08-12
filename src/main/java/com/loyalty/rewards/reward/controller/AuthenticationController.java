package com.loyalty.rewards.reward.controller;

import com.loyalty.rewards.reward.dto.jwt.LoginRequest;
import com.loyalty.rewards.reward.dto.jwt.LoginResponse;
import com.loyalty.rewards.reward.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/login")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){

        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }
}
