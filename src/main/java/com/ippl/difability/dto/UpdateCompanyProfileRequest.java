package com.ippl.difability.dto;

import org.hibernate.validator.constraints.URL;

import com.ippl.difability.enums.IndustryType;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class UpdateCompanyProfileRequest {
    @Size(max = 64)
    private String name;

    @Size(max = 128)
    private String address;

    @Size(max = 256)
    private String description;

    private IndustryType industryType;

    @URL
    private String websiteUrl;

    @Pattern(regexp = "(?i)^.*\\.(jpg|jpeg|png|webp)$")
    private String logoImgPath;
}