package com.loyalty.rewards.reward.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "reset_token"

)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, length = 100)
    private String username;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "used")
    private boolean usedStatus = false;

    public void changeUsedStatus(Boolean usedStatus){
        this.usedStatus = usedStatus;
    }

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
