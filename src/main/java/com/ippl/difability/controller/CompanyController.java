package com.ippl.difability.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.request.CompanyProfileRequest;
import com.ippl.difability.dto.response.HrCredentialResponse;
import com.ippl.difability.dto.response.user.CompanyDetailResponse;
import com.ippl.difability.dto.response.user.HumanResourceDetailResponse;
import com.ippl.difability.service.CompanyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/{companyId}/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CompanyDetailResponse> getCompanyProfile(@PathVariable Long companyId){
        CompanyDetailResponse profile = companyService.getCompanyProfile(companyId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me/profile")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<CompanyDetailResponse> getMyProfile(Authentication auth){
        CompanyDetailResponse profile = companyService.getMyProfile(auth.getName());
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/me/profile")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Void> updateCompanyProfile(
            @Valid @RequestBody CompanyProfileRequest request,
            Authentication auth){
        companyService.updateCompanyProfile(auth.getName(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/humanresources")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<HrCredentialResponse> generateHrAccount(
            Authentication auth){
        HrCredentialResponse response = companyService.generateHrAccount(auth.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/humanresources")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<List<HumanResourceDetailResponse>> getHumanResources(
            Authentication auth){
        List<HumanResourceDetailResponse> humanResources = companyService.getHumanResources(auth.getName());
        return ResponseEntity.ok(humanResources);
    }
}