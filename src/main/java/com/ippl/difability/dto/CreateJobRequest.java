package com.ippl.difability.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.EducationLevel;
import com.ippl.difability.enums.PublicationStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CreateJobRequest {
    @NotBlank
    @Size(max = 64)
    private String title;

    @NotBlank
    @Size(max = 512) 
    private String description;

    @NotNull
    @Positive
    private Double salary;

    @NotNull
    private EducationLevel minimumEducation;

    @NotNull
    @Min(0)
    private Integer minimumYearsExperience;

    @Size(max = 8)
    private List<DisabilityType> compatibleDisabilities;

    @NotNull
    @Future
    private LocalDateTime registrationDeadline;

    @NotNull
    private PublicationStatus publicationStatus;
}

