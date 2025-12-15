package com.ippl.difability.dto.response;

import java.time.LocalDateTime;

import com.ippl.difability.enums.Role;

public record UserResponse(
    Long id,
    String username,
    Role role,
    boolean profileCompleted,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){}
