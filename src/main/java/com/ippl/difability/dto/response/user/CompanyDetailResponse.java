package com.ippl.difability.dto.response.user;

import com.ippl.difability.enums.IndustryType;

public record CompanyDetailResponse(
    String companyName,
    String companyDescription,
    String address,
    IndustryType industryType,
    String websiteUrl,
    String logoImagePath
) implements UserDetailsResponse{}
