package com.ippl.difability.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GeneralLoginRequest(
    @NotBlank(message = "Username/Email is required")
    String username,

    @NotBlank(message = "Password is required")
    String password
){}