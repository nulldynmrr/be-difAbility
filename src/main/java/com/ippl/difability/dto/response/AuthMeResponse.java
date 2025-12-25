package com.ippl.difability.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ippl.difability.enums.Role;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthMeResponse(
    Long id,
    Long companyId,
    String fullName,
    String email,
    Role role
){}
