package com.ippl.difability.dto;

import com.ippl.difability.enums.ApplicationStatus;

import jakarta.validation.constraints.Size;
import lombok.Value;

@Value  
public class ReviewApplicationRequest {
    private ApplicationStatus status;

    @Size(max = 256)
    private String hrNotes;
}
