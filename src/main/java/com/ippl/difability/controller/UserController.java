package com.ippl.difability.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.request.CompanyProfileRequest;
import com.ippl.difability.dto.request.HumanResourceProfileRequest;
import com.ippl.difability.dto.request.JobSeekerProfileRequest;
import com.ippl.difability.dto.response.user.CompanyDetailResponse;
import com.ippl.difability.dto.response.user.JobSeekerDetailResponse;
import com.ippl.difability.dto.response.user.UserResponse;
import com.ippl.difability.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsers(){
        List<UserResponse> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long userId,
            Principal principal){
        UserResponse user = userService.getUser(principal.getName(), userId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId,
            Principal principal){
        userService.deleteUser(principal.getName(), userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobseekers/me/profile")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<JobSeekerDetailResponse> getProfileJobSeeker(Authentication auth){
        JobSeekerDetailResponse profile = userService.getProfileJobSeeker(auth.getName());
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/jobseekers/me/profile")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<Void> updateJobSeekerProfile(
            @Valid @RequestBody JobSeekerProfileRequest request,
            Principal principal){
        userService.updateJobSeekerProfile(principal.getName(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/companies/me/profile")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<CompanyDetailResponse> getProfileCompany(Authentication auth){
        CompanyDetailResponse profile = userService.getProfileCompany(auth.getName());
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/companies/me/profile")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Void> updateCompanyProfile(
            @Valid @RequestBody CompanyProfileRequest request,
            Principal principal){
        userService.updateCompanyProfile(principal.getName(), request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/humanresources/me/profile")
    @PreAuthorize("hasAuthority('HUMAN_RESOURCE')")
    public ResponseEntity<Void> updateHumanResourceProfile(
            @Valid @RequestBody HumanResourceProfileRequest request,
            Principal principal){
        userService.updateHumanResourceProfile(principal.getName(), request);
        return ResponseEntity.ok().build();
    }
}
