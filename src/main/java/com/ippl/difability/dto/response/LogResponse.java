package com.ippl.difability.dto.response;

import java.time.LocalDateTime;

public record LogResponse(
    Long id,
    String actorUsername,
    String actorRole,
    String actorAction,
    String description,
    LocalDateTime createdAt
){}
