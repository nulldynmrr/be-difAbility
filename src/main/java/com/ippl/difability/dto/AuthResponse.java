package com.ippl.difability.dto;

import lombok.Value;

@Value
public class AuthResponse {
    private String token;
    private String role;
}
