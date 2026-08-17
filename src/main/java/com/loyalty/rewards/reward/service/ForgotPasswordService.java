package com.loyalty.rewards.reward.service;

import com.loyalty.rewards.reward.dto.forgotpassword.ForgotPasswordRequest;
import com.loyalty.rewards.reward.dto.forgotpassword.ForgotPasswordResponse;
import com.loyalty.rewards.reward.entity.Token;
import com.loyalty.rewards.reward.repository.TokenRepository;
import com.loyalty.rewards.reward.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Transactional
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest passwordRequest){

        String username = passwordRequest.username();

        if(!userRepository.existsByUsername(username)){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        try{
        List<Token> tokens = tokenRepository.findByUsername(username).orElseThrow();
        for(Token token: tokens){
            if(!token.isUsedStatus()){
                token.changeUsedStatus(true);
            }        }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        String tokenHash = hashToken(generateResetToken());

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10);

        Token token = new Token(username, tokenHash, expiration);

        Token savedToken = tokenRepository.save(token);

        return new ForgotPasswordResponse(savedToken.getUsername(), savedToken.getTokenHash(), savedToken.getExpiresAt());

    }

    private String generateResetToken() {
        byte[] randomBytes = new byte[32]; // 256 bits
        SECURE_RANDOM.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    ex
            );
        }
    }
}
