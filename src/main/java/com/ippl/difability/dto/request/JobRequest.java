package com.ippl.difability.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.EducationLevel;
import com.ippl.difability.enums.JobType;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record JobRequest(
    @Size(min = 3, max = 50)
    String title,

    @Size(min = 10, max = 500)
    String jobDescription,

    @Digits(integer = 12, fraction = 2)
    BigDecimal salary,

    EducationLevel minimumEducation,

    JobType jobType,

    @Positive
    Integer minimumYearsExperience,

    List<DisabilityType> compatibleDisabilities,

    LocalDateTime registrationDeadline
){}
