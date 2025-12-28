package com.ippl.difability.dto.response;

public record HrCredentialResponse(
    String generatedUsername,
    String generatedPassword,
    String token
) {}
