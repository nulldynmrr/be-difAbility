package com.ippl.difability.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<List<JobResponse>> getJobs(Authentication auth){
        List<JobResponse> jobs = jobService.getJobs(auth);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJob(
            @PathVariable Long jobId){
        JobResponse job = jobService.getJob(jobId);
        return ResponseEntity.ok(job);
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('HUMAN_RESOURCE') or hasAuthority('COMPANY')")
    public ResponseEntity<Void> createJob(
            @Valid @RequestBody JobRequest request,
            Authentication auth){
        jobService.createJob(auth.getName(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasAuthority('HUMAN_RESOURCE') or hasAuthority('COMPANY')")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long jobId,
            Authentication auth){
        jobService.deletejob(auth.getName(), jobId);
        return ResponseEntity.noContent().build();
    }
}
