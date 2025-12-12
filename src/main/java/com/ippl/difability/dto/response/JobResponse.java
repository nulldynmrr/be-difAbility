package com.ippl.difability.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.EducationLevel;
import com.ippl.difability.enums.JobType;
import com.ippl.difability.enums.PublicationStatus;

public record JobResponse(
    Long id,
    Long companyId,
    String companyName,
    String companyLogoImagePath,
    String title,
    String jobDescription,
    BigDecimal salary,
    EducationLevel minimumEducation,
    JobType jobType,
    Integer minimumYearsExperience,
    List<DisabilityType> compatibleDisabilities,
    LocalDateTime registrationDeadline,
    PublicationStatus publicationStatus
){}
