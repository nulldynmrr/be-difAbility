package com.ippl.difability.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EducationLevel {
    HIGH_SCHOOL(1, "High School"),
    COLLEGE_STUDENT(2, "College Student"),
    BACHELOR(3, "Bachelor"),
    MASTER(4, "Master"),
    DOCTORATE(5, "Doctorate");

    private final int rank;

    @JsonValue
    private final String label;

    public boolean isHigherOrEqual(EducationLevel other) {
        return this.rank >= other.rank;
    }
}
