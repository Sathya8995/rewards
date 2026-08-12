package com.loyalty.rewards.reward.controller;

import com.loyalty.rewards.reward.dto.register.RegisterRequest;
import com.loyalty.rewards.reward.dto.register.RegisterResponse;
import com.loyalty.rewards.reward.service.UserRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/register")
@Tag(
        name = "Users",
        description = "APIs for registering users"
)
public class UserController {

    private final UserRegisterService userRegisterService;

    public UserController(UserRegisterService userRegisterService){
        this.userRegisterService=userRegisterService;
    }

    @Operation(
            summary = "Register a User",
            description = "Creates a new user with CUSTOMER role and Enabled status"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User Already exists"
            )
    })
    @PostMapping
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userRegisterService.registerUser(registerRequest));
    }
}
