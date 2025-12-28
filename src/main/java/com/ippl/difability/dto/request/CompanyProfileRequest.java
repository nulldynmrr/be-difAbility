package com.ippl.difability.dto.request;

import com.ippl.difability.enums.IndustryType;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record CompanyProfileRequest(
    @Size(min = 3, max = 50)
    String companyName,

    @Size(min = 10, max = 500)
    String companyDescription,

    @Size(min = 5, max = 150)
    String address,

    IndustryType industryType,

    @URL
    String websiteUrl,

    @URL
    String linkedinUrl,

    @URL
    String youtubeUrl,

    @URL
    String instagramUrl,

    @URL
    String twitterUrl,
    
    String logoImagePath,

    Boolean agreeToTerms
){}