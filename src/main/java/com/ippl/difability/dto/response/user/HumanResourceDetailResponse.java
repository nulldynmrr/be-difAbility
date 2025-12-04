package com.ippl.difability.dto.response.user;

public record HumanResourceDetailResponse(
    String fullName,
    String contact,
    String ppImagePath
) implements UserDetailsResponse{}
