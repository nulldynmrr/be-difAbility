package com.ippl.difability.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class LoginRequest {
    @NotBlank
    private String identifier;
    @NotBlank
    private String password;
}
