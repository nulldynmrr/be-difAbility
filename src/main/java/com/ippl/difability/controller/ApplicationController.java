package com.ippl.difability.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.request.ApplicationRequest;
import com.ippl.difability.dto.request.ApplicationReviewRequest;
import com.ippl.difability.service.ApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping("/jobs/{jobId}/applications")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<Void> createApplication(
            @PathVariable Long jobId,
            @Valid @RequestBody ApplicationRequest request,
            Principal principal){
        applicationService.createApplication(principal.getName(), jobId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/applications/{applicationId}")
    @PreAuthorize("hasAuthority('HUMAN_RESOURCE')")
    public ResponseEntity<Void> reviewApplication(
            @PathVariable Long applicationId,
            @RequestBody ApplicationReviewRequest request,
            Principal principal){
        applicationService.reviewApplication(principal.getName(), applicationId, request);
        return ResponseEntity.ok().build();
    }
}
