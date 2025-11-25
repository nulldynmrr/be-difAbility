package com.ippl.difability.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.HrAccountResponse;
import com.ippl.difability.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/company/hr-accounts")
    @PreAuthorize("hasRole('COMPANY')")
    public HrAccountResponse generateHr(@AuthenticationPrincipal UserDetails principal){
        String identifier = principal.getUsername();
        return companyService.generateHr(identifier);
    }
}
