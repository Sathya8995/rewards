package com.loyalty.rewards.reward.service;

import com.loyalty.rewards.reward.dto.resetpassword.ResetPasswordRequest;
import com.loyalty.rewards.reward.entity.Token;
import com.loyalty.rewards.reward.entity.User;
import com.loyalty.rewards.reward.exception.InvalidResetTokenException;
import com.loyalty.rewards.reward.exception.PasswordReuseException;
import com.loyalty.rewards.reward.repository.TokenRepository;
import com.loyalty.rewards.reward.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordService {

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest){

        String rawResetToken = resetPasswordRequest.resetToken();

        String hashedResetToken = hashToken(rawResetToken);

        Token resetToken = tokenRepository.findByTokenHash(hashedResetToken)
                .orElseThrow(() -> new InvalidResetTokenException("Entered reset token is invalid"));

        if(resetToken.isUsedStatus()){
            throw new InvalidResetTokenException("Entered reset token is invalid");
        }

        LocalDateTime now = LocalDateTime.now();

        if(resetToken.getExpiresAt().isBefore(now)){
            throw new InvalidResetTokenException("Reset token is expired");
        }

        String username = resetToken.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

        if (passwordEncoder.matches(
                resetPasswordRequest.newPassword(),
                user.getPassword()
        )) {
            throw new PasswordReuseException(
                    "New password cannot be the same as current password"
            );
        }

        user.changePassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        User savedUser = userRepository.save(user);

        resetToken.changeUsedStatus(true);
        Token savedToken = tokenRepository.save(resetToken);
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
