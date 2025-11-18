package com.ippl.difability.dto;

import java.util.List;

import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.EducationLevel;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class UpdateJobSeekerProfileRequest {
    @Size(max = 64)
    private String name;

    @Size(max = 128)
    private String address;

    @Size(max = 256)
    private String about;

    private DisabilityType disabilityType;
    private EducationLevel educationLevel;

    @Size(max = 8)
    private List<
        @Size(max = 32) String
        > skills;

    @Pattern(regexp = "(?i)^.*\\.(jpg|jpeg|png)$")
    private String ppImgPath;

    @Pattern(regexp = "(?i)^.*\\.(pdf)$")
    private String cvFilePath;

    private List<@Pattern(regexp = "(?i)^.*\\.(pdf)$") String> certifFilePaths;
}