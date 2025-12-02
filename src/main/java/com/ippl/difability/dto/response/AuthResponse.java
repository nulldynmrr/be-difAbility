package com.ippl.difability.dto.response;

import com.ippl.difability.enums.Role;

public record AuthResponse(
    String token,
    Long id,
    String username,
    Role role,
    boolean profileCompleted
){}