package com.ippl.difability.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.UpdateCompanyProfileRequest;
import com.ippl.difability.dto.UpdateJobSeekerProfileRequest;
import com.ippl.difability.entity.User;
import com.ippl.difability.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/jobseeker-profile")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public User updateJobSeekerProfile(
        @AuthenticationPrincipal UserDetails principal,
        @Valid @RequestBody UpdateJobSeekerProfileRequest request){
        
        String identifier = principal.getUsername();

        return userService.updateJobSeekerProfile(identifier, request);
    }

    @PutMapping("/company-profile")
    @PreAuthorize("hasRole('COMPANY')")
    public User updateCompanyProfile(
        @AuthenticationPrincipal UserDetails principal,
        @Valid @RequestBody UpdateCompanyProfileRequest request){
        
        String identifier = principal.getUsername();

        return userService.updateCompanyProfile(identifier, request);
    }
}
