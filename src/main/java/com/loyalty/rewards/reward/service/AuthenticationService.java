package com.loyalty.rewards.reward.service;

import com.loyalty.rewards.reward.dto.login.LoginRequest;
import com.loyalty.rewards.reward.dto.login.LoginResponse;
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

        log.debug("Authenticating User");
        Authentication authentication =
                authenticationManager.authenticate
                        (new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password())
                        );
        log.info("Authentication successful");

        log.debug("Generating Access Token");
        String accessToken =
                jwtService.generateToken((UserDetails) authentication.getPrincipal());
        log.info("Access Token generation successful");

        log.debug("Generating Refresh Token");
        String refreshToken =
                jwtService.generateRefreshToken((UserDetails) authentication.getPrincipal());
        log.info("Refresh Token generation successful");

        long expiresIn = jwtService.getExpirationSeconds();

        return new LoginResponse(accessToken, refreshToken, "Bearer", expiresIn);

    }

}
