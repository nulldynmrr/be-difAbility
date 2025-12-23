package com.ippl.difability.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.request.JobSeekerProfileRequest;
import com.ippl.difability.dto.response.user.JobSeekerDetailResponse;
import com.ippl.difability.service.JobSeekerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobseekers")
@RequiredArgsConstructor
public class JobSeekerController {
    private final JobSeekerService jobSeekerService;

    @GetMapping("/{jobseekerId}/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JobSeekerDetailResponse> getJobSeekerProfile(@PathVariable Long jobseekerId){
        JobSeekerDetailResponse profile = jobSeekerService.getJobSeekerProfile(jobseekerId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me/profile")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<JobSeekerDetailResponse> getMyProfile(Authentication auth){
        JobSeekerDetailResponse profile = jobSeekerService.getMyProfile(auth.getName());
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/me/profile")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<Void> updateJobSeekerProfile(
            @Valid @RequestBody JobSeekerProfileRequest request,
            Authentication auth){
        jobSeekerService.updateJobSeekerProfile(auth.getName(), request);
        return ResponseEntity.ok().build();
    }
}
