package com.ippl.difability.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PublicationStatus {
    OPEN("Open"),
    CLOSED("Closed");

    @JsonValue
    private final String label;
}
