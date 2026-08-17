package com.loyalty.rewards.reward.repository;

import com.loyalty.rewards.reward.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByTokenHash(String tokenHash);
}
