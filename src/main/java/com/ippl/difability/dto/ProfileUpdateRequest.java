package com.ippl.difability.dto;

import java.util.List;

import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.EducationLevel;
import com.ippl.difability.enums.IndustryType;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequest {
    @Size(max = 64, message = "Name cannot exceed 64 characters")
    private String name;

    @Size(max = 128, message = "Address cannot exceed 128 characters")
    private String address;
    // jobseeker
    @Size(max = 256, message = "About section cannot exceed 256 characters")
    private String about;

    private DisabilityType disabilityType;

    @Size(max = 8, message = "You may include up to 8 skills")
    private List<
            @Size(max = 32, message = "Each skill cannot exceed 32 characters")
            String> skills;

    private EducationLevel educationLevel;

    @Pattern(
        regexp = "^.*\\.(jpg|jpeg|png)$",
        message = "Profile picture must be an image file"
    )
    private String ppImgPath;

    @Pattern(
        regexp = "^.*\\.(pdf)$",
        message = "CV must be a PDF file"
    )
    private String cvFilePath;

    private List<
            @Pattern(
                regexp = "^.*\\.(pdf)$",
                message = "Certificate files must be PDF"
            )
            String> certifFilePaths;

    // company
    @Size(max = 256, message = "Description cannot exceed 256 characters")
    private String description;

    private IndustryType industryType;

    @Pattern(
        regexp = "^(https?://).+$",
        message = "Website must be a valid URL"
    )
    private String websiteUrl;

    @Pattern(
        regexp = "^.*\\.(jpg|jpeg|png|webp)$",
        message = "Logo must be an image file"
    )
    private String logoImgPath;
}
