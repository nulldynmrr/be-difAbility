package com.ippl.difability.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.entity.Application;
import com.ippl.difability.service.ApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/application/")
@RequiredArgsConstructor
public class ApplicationController {
    public final ApplicationService applicationService;

    @PostMapping("{jobId}")
    public Application apply(
        @PathVariable Long jobId,
        @AuthenticationPrincipal UserDetails principal){
        
        String identifier = principal.getUsername();
        
        return applicationService.createApplication(identifier, jobId);
    }
}
