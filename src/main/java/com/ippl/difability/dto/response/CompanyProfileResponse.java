package com.ippl.difability.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyProfileResponse {
    private String name;
    private String description;
    private String address;
    private String industryType;
    private String websiteUrl;
    private String linkedinUrl;
    private String youtubeUrl;
    private String instagramUrl;
    private String twitterUrl;
    private String logoImgPath;
}
