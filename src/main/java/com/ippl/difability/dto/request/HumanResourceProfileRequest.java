package com.ippl.difability.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HumanResourceProfileRequest(
    @Size(min = 3, max = 50)
    String fullName,

    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[0-9]$")
    String contact
){}