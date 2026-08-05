package com.loyalty.rewards.reward.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank(message = "Username is required")
                              @Size(
                                      min = 4,
                                      max = 100,
                                      message = "Username must be between 4 and 100 characters"
                              )
                              String username,

                              @NotBlank(message = "Password is required")
                              @Size(
                                      min = 8,
                                      max = 72,
                                      message = "Password must be between 8 and 72 characters"
                              )
                              String password) {
}
