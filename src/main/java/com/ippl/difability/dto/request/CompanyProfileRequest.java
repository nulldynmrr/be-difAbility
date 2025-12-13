package com.ippl.difability.dto.request;

import org.hibernate.validator.constraints.URL;
import com.ippl.difability.enums.IndustryType;
import jakarta.validation.constraints.Size;

public record CompanyProfileRequest(
    @Size(max = 50)
    String companyName,

    @Size(max = 500)
    String companyDescription,

    @Size(max = 150)
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
