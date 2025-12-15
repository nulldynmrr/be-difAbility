package com.ippl.difability.dto.request;

import com.ippl.difability.enums.IndustryType;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record CompanyProfileRequest(
    @Size(max = 50)
    String companyName,

    @Size(max = 500)
    String companyDescription,

    @Size(max = 150)
    String address,

    IndustryType industryType,

    @Pattern(regexp = "^(https?://.*)?$", message = "Website URL must be a valid URL or empty")
    String websiteUrl,

    @Pattern(regexp = "^(https?://.*)?$", message = "LinkedIn URL must be a valid URL or empty")
    String linkedinUrl,

    @Pattern(regexp = "^(https?://.*)?$", message = "YouTube URL must be a valid URL or empty")
    String youtubeUrl,

    @Pattern(regexp = "^(https?://.*)?$", message = "Instagram URL must be a valid URL or empty")
    String instagramUrl,

    @Pattern(regexp = "^(https?://.*)?$", message = "Twitter URL must be a valid URL or empty")
    String twitterUrl,

    String logoImagePath,

    Boolean agreeToTerms
){}
