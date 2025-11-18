package com.ippl.difability.dto;

import lombok.Value;

@Value
public class HrAccountResponse {
    private String username;
    private String password;
    private String role;
}
