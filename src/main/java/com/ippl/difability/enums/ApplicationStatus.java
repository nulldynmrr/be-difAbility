package com.ippl.difability.enums;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationStatus {
    UNDER_REVIEW("Under Review"),
    ACCEPTED("Accepted"),
    DECLINED("Declined");

    @JsonValue
    private final String label;
}