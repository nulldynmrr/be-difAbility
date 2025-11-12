package com.ippl.difability.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DisabilityType {
    VISUAL("Visual"),
    HEARING("Hearing"),
    MOBILITY("Mobility"),
    COGNITIVE("Cognitive");

    @JsonValue
    private final String label;
}
