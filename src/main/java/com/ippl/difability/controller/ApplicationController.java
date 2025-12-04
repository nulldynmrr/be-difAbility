package com.ippl.difability.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.request.ApplicationRequest;
import com.ippl.difability.dto.request.ApplicationReviewRequest;
import com.ippl.difability.dto.response.ApplicationResponse;
import com.ippl.difability.service.ApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping("/jobs/{jobId}/applications/{applicationId}")
    @PreAuthorize("hasAuthority('HUMAN_RESOURCE')")
    public ResponseEntity<ApplicationResponse> getApplication(
            @PathVariable Long jobId,
            @PathVariable Long applicationId,
            Principal principal){
        ApplicationResponse application = applicationService.getApplication(principal.getName(), jobId, applicationId);
        return ResponseEntity.ok(application);
    }

    @PostMapping("/jobs/{jobId}/applications")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<Void> createApplication(
            @PathVariable Long jobId,
            @Valid @RequestBody ApplicationRequest request,
            Principal principal){
        applicationService.createApplication(principal.getName(), jobId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/jobs/{jobId}/applications/{applicationId}")
    @PreAuthorize("hasAuthority('HUMAN_RESOURCE')")
    public ResponseEntity<Void> reviewApplication(
            @PathVariable Long jobId,
            @PathVariable Long applicationId,
            @RequestBody ApplicationReviewRequest request,
            Principal principal){
        applicationService.reviewApplication(principal.getName(), jobId, applicationId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/applications/{applicationId}")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long applicationId,
            Principal principal){
        applicationService.deleteApplication(principal.getName(), applicationId);
        return ResponseEntity.noContent().build();
    }
}
