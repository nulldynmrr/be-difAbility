package com.ippl.difability.dto.response;

import java.time.LocalDateTime;

import com.ippl.difability.enums.ApplicationStatus;

public record ApplicationResponse(
    Long applicationId,
    Long jobId,
    Long jobSeekerId,
    String cvDocumentPath,
    String coverLetter,
    String hrNotes,
    ApplicationStatus applicationStatus,
    LocalDateTime appliedAt
){}