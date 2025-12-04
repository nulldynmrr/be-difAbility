package com.ippl.difability.dto.response.user;

import java.util.List;

import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.EducationLevel;

public record JobSeekerDetailResponse(
    String fullname,
    String about,
    String address,
    DisabilityType disabilityType,
    List<String> skills,
    List<String> certificationFilePaths,
    EducationLevel educationLevel,
    String ppImagePath,
    String cvDocumentPath 
) implements UserDetailsResponse {}
