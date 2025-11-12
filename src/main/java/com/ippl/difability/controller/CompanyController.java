package com.ippl.difability.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.HrAccountResponse;
import com.ippl.difability.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
    private final AuthService authService;

    @PostMapping("/generate-hr")
    public HrAccountResponse generateHr(@RequestParam String companyEmail) {
        return authService.generateHr(companyEmail);
    }
}
