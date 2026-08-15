package com.loyalty.rewards.reward.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "reset_tokens",
        uniqueConstraints = {
            @UniqueConstraint(
                name = "uk_reset_tokens_username",
                columnNames = "username"
            )
        }
)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime  expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public Token(String username, String tokenHash, LocalDateTime  expiresAt){
        this.username = username;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
    }
}
