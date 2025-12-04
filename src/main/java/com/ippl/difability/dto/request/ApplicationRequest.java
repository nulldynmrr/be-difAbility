package com.ippl.difability.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ApplicationRequest(
    @NotBlank(message = "Cover letter is required.")
    @Size(min = 3, max = 500)
    String coverLetter
){}
