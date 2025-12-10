package com.ippl.difability.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.response.HrCredentialResponse;
import com.ippl.difability.dto.response.user.HumanResourceDetailResponse;
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

    @GetMapping("/companies/me/humanresources")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<List<HumanResourceDetailResponse>> getHumanResources(
            Principal principal){
        List<HumanResourceDetailResponse> humanResources = companyService.getHumanResources(principal.getName());
        return ResponseEntity.ok(humanResources);
    }
}
