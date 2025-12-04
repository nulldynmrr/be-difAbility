package com.ippl.difability.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @GetMapping("/{jobId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JobResponse> getJob(
            @PathVariable Long jobId){
        JobResponse job = jobService.getJob(jobId);
        return ResponseEntity.ok(job);
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('HUMAN_RESOURCE')")
    public ResponseEntity<Void> createJob(
            @Valid @RequestBody JobRequest request,
            Principal principal){
        jobService.createJob(principal.getName(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/jobs/{jobId}")
    @PreAuthorize("hasAuthority('HUMAN_RESOURCE') or hasAuthority('COMPANY')")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long jobId,
            Principal principal){
        jobService.deletejob(principal.getName(), jobId);
        return ResponseEntity.noContent().build();
    }
}
