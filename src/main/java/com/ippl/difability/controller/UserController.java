package com.ippl.difability.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.request.CompanyProfileRequest;
import com.ippl.difability.dto.request.HumanResourceProfileRequest;
import com.ippl.difability.dto.request.JobSeekerProfileRequest;
import com.ippl.difability.dto.response.LogResponse;
import com.ippl.difability.dto.response.UserResponse;
import com.ippl.difability.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @PatchMapping("/me/profile/jobseeker")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<Void> updateJobSeekerProfile(
            @Valid @RequestBody JobSeekerProfileRequest request,
            Principal principal){
        userService.updateJobSeekerProfile(principal.getName(), request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/profile/company")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Void> updateCompanyProfile(
            @Valid @RequestBody CompanyProfileRequest request,
            Principal principal){
        userService.updateCompanyProfile(principal.getName(), request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/profile/humanresource")
    @PreAuthorize("hasAuthority('HUMAN_RESOURCE')")
    public ResponseEntity<Void> updateHumanResourceProfile(
            @Valid @RequestBody HumanResourceProfileRequest request,
            Principal principal){
        userService.updateHumanResourceProfile(principal.getName(), request);
        return ResponseEntity.ok().build();
    }
}
