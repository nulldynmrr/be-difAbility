package com.ippl.difability.dto.request;

import com.ippl.difability.enums.ApplicationStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApplicationReviewRequest(
    @Size(min = 10, max = 500)
    String hrNotes,

    @NotNull
    ApplicationStatus status
){}
    

