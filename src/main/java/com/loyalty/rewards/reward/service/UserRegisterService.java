package com.loyalty.rewards.reward.service;

import com.loyalty.rewards.reward.dto.register.RegisterRequest;
import com.loyalty.rewards.reward.dto.register.RegisterResponse;
import com.loyalty.rewards.reward.entity.Role;
import com.loyalty.rewards.reward.entity.User;
import com.loyalty.rewards.reward.exception.UserAlreadyExistsException;
import com.loyalty.rewards.reward.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserRegisterService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserRegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse registerUser(RegisterRequest registerRequest){

        boolean exists = userRepository.existsByUsername(registerRequest.username());

        if(exists){
            throw new UserAlreadyExistsException(registerRequest.username());
        }

        String password = passwordEncoder.encode(registerRequest.password());

        log.debug(
                "Creating user for username={}",
                registerRequest.username()
        );
        User user = new User(registerRequest.username(), password, Role.CUSTOMER);

        User savedUser = userRepository.save(user);
        log.info("User created successfully, userId={}, userName={}",
                savedUser.getId(),
                savedUser.getUsername());
        return mapToResponse(savedUser);

    }

    private RegisterResponse mapToResponse(User user){

        return new RegisterResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
