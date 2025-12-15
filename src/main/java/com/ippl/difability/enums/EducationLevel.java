package com.ippl.difability.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EducationLevel {
    // sma, kuliah, s1, s2, s3
    HIGH_SCHOOL("High School"),
    COLLEGE_STUDENT("College Student"),
    BACHELOR("Bachelor"),
    MASTER("Master"),
    DOCTORATE("Doctorate");

    @JsonValue
    private final String label;
}
