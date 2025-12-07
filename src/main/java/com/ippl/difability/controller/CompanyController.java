package com.ippl.difability.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.response.HrCredentialResponse;
import com.ippl.difability.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/companies/me/humanresources")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<HrCredentialResponse> generateHrAccount(
            Principal principal){
        HrCredentialResponse response = companyService.generateHrAccount(principal.getName());
        return ResponseEntity.ok(response);
    }
}
