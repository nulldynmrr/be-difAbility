package com.ippl.difability.dto.response;

import java.time.LocalDateTime;

public record ApplicationResponse(
    Long applicationId,
    Long jobId,
    Long jobSeekerId,
    String cvDocumentPath,
    String coverLetter,
    LocalDateTime appliedAt
){}