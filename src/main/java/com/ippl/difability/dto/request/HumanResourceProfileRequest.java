package com.ippl.difability.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HumanResourceProfileRequest(
    @Size(max = 50)
    String fullName,

    @Size(max = 15)
    @Pattern(regexp = "^[0-9]$")
    String contact,

    String ppImagePath
){}