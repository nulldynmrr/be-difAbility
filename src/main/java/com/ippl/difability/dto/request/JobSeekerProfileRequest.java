package com.ippl.difability.dto.request;

import java.util.List;

import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.EducationLevel;
import com.ippl.difability.enums.JobType;

import jakarta.validation.constraints.Size;

public record JobSeekerProfileRequest(
    @Size(max = 50)
    String fullName,

    @Size(max = 500)
    String about,

    @Size(max = 150)
    String address,

    DisabilityType disabilityType,

    List<String> skills,

    List<String> certificationFilePaths,

    EducationLevel educationLevel,

    @Size(max = 15)
    String academicYear,

    JobType jobType,
    
    String ppImagePath, 
    
    String cvDocumentPath
){}
