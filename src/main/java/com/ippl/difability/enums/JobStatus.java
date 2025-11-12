package com.ippl.difability.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobStatus {
    NEW("New"),
    ONGOING("On Going"),
    DONE("Done");

    @JsonValue
    private final String label;
}
