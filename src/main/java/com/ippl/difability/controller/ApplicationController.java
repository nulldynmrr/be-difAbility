package com.ippl.difability.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.ReviewApplicationRequest;
import com.ippl.difability.dto.ReviewApplicationResponse;
import com.ippl.difability.entity.Application;
import com.ippl.difability.service.ApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/application/")
@RequiredArgsConstructor
public class ApplicationController {
    public final ApplicationService applicationService;

    @PostMapping("{jobId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public Application createApplication(
        @PathVariable Long jobId,
        @AuthenticationPrincipal UserDetails principal){

        String identifier = principal.getUsername();
        return applicationService.createApplication(identifier, jobId);
    }

    @PatchMapping("/{applicationId}/review")
    @PreAuthorize("hasRole('HUMAN_RESOURCE')")
    public ReviewApplicationResponse reviewApplication(
        @PathVariable Long applicationId,
        @AuthenticationPrincipal UserDetails principal,
        @Valid @RequestBody ReviewApplicationRequest request){

        String identifier = principal.getUsername();
        return applicationService.reviewApplication(identifier, applicationId, request);
    }
}
