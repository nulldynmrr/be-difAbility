package com.ippl.difability.dto;

import com.ippl.difability.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

@Value
public class RegisterRequest {
    @Email(message = "Invalid email format.")
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Password must be at least 8 characters long: with 1 uppercase and 1 number")
    private String password;

    @NotNull(message = "Role cannot be null")
    private Role role;
}
