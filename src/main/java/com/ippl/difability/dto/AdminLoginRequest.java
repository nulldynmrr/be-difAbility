package com.ippl.difability.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String otp;
}
