package com.loyalty.rewards.reward.security.runner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class PasswordHashGeneratorTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void generateHashes() {
        System.out.println(
                "admin123 = " + passwordEncoder.encode("admin123")
        );

        System.out.println(
                "customer123 = " + passwordEncoder.encode("customer123")
        );
    }
}
