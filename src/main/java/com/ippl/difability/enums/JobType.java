package com.ippl.difability.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum  JobType {
    FULL_TIME("Full Time"),
    FREELANCE("Freelance"),
    CONTRACT("Contract"),
    REMOTE("Remote"),
    INTERNSHIP("Internship");

    @JsonValue
    private final String label; 
}