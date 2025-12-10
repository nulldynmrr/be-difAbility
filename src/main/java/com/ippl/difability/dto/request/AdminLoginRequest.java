package com.ippl.difability.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminLoginRequest(
    @NotBlank(message = "Username is required")
    String username,

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", 
             message = "Password must contain at least one uppercase letter and one number")
    String password,

    @NotBlank(message = "OTP Code is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    @Pattern(regexp = "^\\d{6}$", message = "OTP must contain only numbers")
    String otp
){}