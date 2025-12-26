package com.ippl.difability.controller;

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

import com.ippl.difability.dto.request.HumanResourceProfileRequest;
import com.ippl.difability.dto.response.user.HumanResourceDetailResponse;
import com.ippl.difability.service.HumanResourceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/humanresources")
@RequiredArgsConstructor
public class HumanResourceController {
    private final HumanResourceService humanResourceService;

    @GetMapping("/{hrId}/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HumanResourceDetailResponse> getHrProfile(@PathVariable Long hrId){
        HumanResourceDetailResponse profile = humanResourceService.getHrProfile(hrId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me/profile")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<HumanResourceDetailResponse> getMyProfile(Authentication auth){
        HumanResourceDetailResponse profile = humanResourceService.getMyProfile(auth.getName());
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/me/profile")
    @PreAuthorize("hasAuthority('HUMAN_RESOURCE')")
    public ResponseEntity<Void> updateHumanResourceProfile(
            @Valid @RequestBody HumanResourceProfileRequest request,
            Authentication auth){
        humanResourceService.updateHumanResourceProfile(auth.getName(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{hrId}")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Void> deleteHumanResource(
            @PathVariable Long hrId,
            Authentication auth){
        humanResourceService.deleteHumanResource(auth.getName(), hrId);
        return ResponseEntity.noContent().build();
    }
}
