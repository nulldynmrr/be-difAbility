package com.ippl.difability.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.HrAccountResponse;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.User;
import com.ippl.difability.repository.UserRepository;
import com.ippl.difability.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/generate-hr")
    @PreAuthorize("hasRole('COMPANY')")
    public HrAccountResponse generateHr(@AuthenticationPrincipal UserDetails principal) {
    String identifier = principal.getUsername();
        User user = userRepository.findByIdentifier(identifier)
            .orElseThrow(() -> new IllegalStateException("User not found"));

        if(!(user instanceof Company)){
            throw new IllegalStateException("Authenticated user is not a company");
        }

        Company company = (Company) user;

        return authService.generateHr(company);
    }
}
