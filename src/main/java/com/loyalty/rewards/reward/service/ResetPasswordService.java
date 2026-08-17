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

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordService {

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest){

        String resetToken = resetPasswordRequest.resetToken();

        Token token = tokenRepository.findByTokenHash(resetToken)
                .orElseThrow(() -> new InvalidResetTokenException("Entered reset token is invalid"));

        if(token.isUsedStatus()){
            throw new InvalidResetTokenException("Entered reset token is invalid");
        }

        LocalDateTime now = LocalDateTime.now();

        if(token.getExpiresAt().isBefore(now)){
            throw new InvalidResetTokenException("Reset token is expired");
        }

        String username = token.getUsername();

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

        token.changeUsedStatus(true);
        Token savedToken = tokenRepository.save(token);
    }
}
