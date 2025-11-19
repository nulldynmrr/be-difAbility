package com.ippl.difability.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.CreateJobRequest;
import com.ippl.difability.entity.Job;
import com.ippl.difability.repository.JobRepository;
import com.ippl.difability.service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {
    private final JobRepository jobRepository;
    private final JobService jobService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('HUMAN_RESOURCE')")
    public Job createJob(
        @AuthenticationPrincipal UserDetails principal,
        @Valid @RequestBody CreateJobRequest request){
        
        String identifier = principal.getUsername();
        
        return jobService.createJob(identifier, request);
    }

    @GetMapping("/list")
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
}
