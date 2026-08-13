package com.loyalty.rewards.reward.service;

import com.loyalty.rewards.reward.dto.changepassword.ChangePasswordRequest;
import com.loyalty.rewards.reward.entity.User;
import com.loyalty.rewards.reward.exception.InvalidCurrentPasswordException;
import com.loyalty.rewards.reward.exception.PasswordReuseException;
import com.loyalty.rewards.reward.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChangePasswordService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void changePassword(ChangePasswordRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with username: " + username
                        )
                );

        validateNewPassword(
                request.currentPassword(),
                request.newPassword()
        );

        validateCurrentPassword(
                request.currentPassword(),
                user.getPassword()
        );

        String encodedNewPassword =
                passwordEncoder.encode(request.newPassword());

        user.changePassword(encodedNewPassword);

        log.info(
                "Password changed successfully for username={}",
                username
        );
    }

    private void validateCurrentPassword(
            String currentPassword,
            String storedPasswordHash
    ) {

        if (!passwordEncoder.matches(
                currentPassword,
                storedPasswordHash
        )) {
            throw new InvalidCurrentPasswordException(
                    "Current password is incorrect"
            );
        }
    }

    private void validateNewPassword(
            String currentPassword,
            String newPassword
    ) {

        if (currentPassword.equals(newPassword)) {
            throw new PasswordReuseException(
                    "New password cannot be the same as current password"
            );
        }
    }
}
