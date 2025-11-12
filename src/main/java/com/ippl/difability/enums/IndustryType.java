package com.ippl.difability.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IndustryType {
    TECHNOLOGY("Technology"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    FINANCE("Finance"),
    ECOMMERCE("E-Commerce"),
    MEDIA("Media"),
    OTHERS("Others");

    @JsonValue
    private final String label;
}
