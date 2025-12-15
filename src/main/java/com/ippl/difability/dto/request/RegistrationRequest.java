package com.ippl.difability.dto.request;

import com.ippl.difability.enums.Role;
import com.ippl.difability.validation.AllowedRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", 
             message = "Password must contain at least one uppercase letter and one number")
    String password,

    @NotNull(message = "Role is required")
    @AllowedRole(message = "Invalid role")
    Role role
){}