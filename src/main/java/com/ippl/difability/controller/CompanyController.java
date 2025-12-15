package com.ippl.difability.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ippl.difability.dto.request.CompanyProfileRequest;
import com.ippl.difability.dto.response.CompanyProfileResponse;
import com.ippl.difability.dto.response.HrCredentialResponse;
import com.ippl.difability.service.CompanyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/companies/me/humanresources")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<HrCredentialResponse> generateHrAccount(
            Principal principal) {
        HrCredentialResponse response = companyService.generateHrAccount(principal.getName());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/companies/me/profile")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<?> updateProfile(
            Principal principal,
            @Valid @RequestBody CompanyProfileRequest request) {  
        companyService.updateProfile(principal.getName(), request);
        return ResponseEntity.ok().body(
            java.util.Map.of("message", "Profile updated successfully")
        );
    }

    @GetMapping("/companies/me/profile")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<?> getMyProfile(Principal principal) {
        return ResponseEntity.ok(
        companyService.getMyProfile(principal.getName())
        );
    }

    @PostMapping("/companies/me/profile")
@PreAuthorize("hasAuthority('COMPANY')")
public ResponseEntity<?> createProfile(
        Principal principal,
        @Valid @RequestBody CompanyProfileRequest request) {

    CompanyProfileResponse response = companyService.createProfile(principal.getName(), request);
    return ResponseEntity.ok(response);
}

}