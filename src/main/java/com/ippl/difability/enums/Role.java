package com.ippl.difability.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("Admin"),
    COMPANY("Company"),
    JOB_SEEKER("Job Seeker"),
    HUMAN_RESOURCE("Human Resource");

    @JsonValue
    private final String label;
}