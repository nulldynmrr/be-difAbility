package com.ippl.difability.dto;

import java.time.LocalDateTime;

import lombok.Value;

@Value
public class ReviewApplicationResponse {
    private String jobTitle;
    private String jobSeekerName;
    private String status;
    private String hrNotes;
    private LocalDateTime appliedAt;
}
