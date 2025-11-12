package com.ippl.difability.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HrAccountResponse {
    private final String username;
    private final String password;
    private final String role;
}
