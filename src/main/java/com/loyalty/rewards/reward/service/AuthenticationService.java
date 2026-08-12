package com.loyalty.rewards.reward.service;

import com.loyalty.rewards.reward.dto.jwt.LoginRequest;
import com.loyalty.rewards.reward.dto.jwt.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public LoginResponse login(LoginRequest request){

        Authentication authentication =
                authenticationManager.authenticate
                        (new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password())
                        );

        String accessToken =
                jwtService.generateToken((UserDetails) authentication.getPrincipal());

        long expiresIn = jwtService.getExpirationSeconds();

        return new LoginResponse(accessToken, "Bearer", expiresIn);

    }

}
