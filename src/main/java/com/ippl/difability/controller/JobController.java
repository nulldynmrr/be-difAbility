package com.ippl.difability.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.request.JobRequest;
import com.ippl.difability.dto.response.JobResponse;
import com.ippl.difability.service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<JobResponse>> getJobs(Principal principal){
        List<JobResponse> jobs = jobService.getJobs(principal.getName());
        return ResponseEntity.ok(jobs);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('HUMAN_RESOURCE')")
    public ResponseEntity<Void> createJob(
            @Valid @RequestBody JobRequest request,
            Principal principal){
        jobService.createJob(principal.getName(), request);
        return ResponseEntity.ok().build();
    }
}
