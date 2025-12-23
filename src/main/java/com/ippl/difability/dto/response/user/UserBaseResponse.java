package com.ippl.difability.dto.response.user;

import java.time.LocalDateTime;

import com.ippl.difability.enums.Role;

public record UserBaseResponse(
    Long id,
    String username,
    Role role,
    boolean profileCompleted,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){}
