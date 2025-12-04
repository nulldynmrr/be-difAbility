package com.ippl.difability.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
    UserBaseResponse base,
    UserDetailsResponse details
){}

