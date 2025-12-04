package com.ippl.difability.dto.response.user;

public sealed interface UserDetailsResponse
    permits
        JobSeekerDetailResponse,
        HumanResourceDetailResponse,
        CompanyDetailResponse{}
