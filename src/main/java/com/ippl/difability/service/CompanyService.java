package com.ippl.difability.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ippl.difability.dto.request.CompanyProfileRequest;
import com.ippl.difability.dto.response.CompanyProfileResponse;
import com.ippl.difability.dto.response.HrCredentialResponse;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.enums.Role;
import com.ippl.difability.exception.UserNotFoundException;
import com.ippl.difability.repository.CompanyRepository;
import com.ippl.difability.repository.HumanResourceRepository;
import com.ippl.difability.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    private final LogService logService;
    private final CompanyRepository companyRepository;
    private final HumanResourceRepository humanResourceRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public HrCredentialResponse generateHrAccount(String username) {
        Company company = companyRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        String newUsername = generateUsername(username);
        String newPassword = generatePassword();

        HumanResource humanResource = new HumanResource();
        humanResource.setUsername(newUsername);
        humanResource.setPassword(passwordEncoder.encode(newPassword));
        humanResource.setRole(Role.HUMAN_RESOURCE);
        humanResource.setCompany(company);
        humanResourceRepository.save(humanResource);

        logService.log(
            username,
            company.getRole().name(), 
            "CREATE_HR", 
            "Generated new HR account: " + newUsername
        );
        return new HrCredentialResponse(newUsername, newPassword);
    }

   public void updateProfile(String username, CompanyProfileRequest request) {
    Company company = companyRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);

    // For PATCH: Only update fields that are provided (not null)
    if (request.companyName() != null) {
        company.setCompanyName(request.companyName().isBlank() ? "" : request.companyName());
    }
    if (request.companyDescription() != null) {
        company.setCompanyDescription(request.companyDescription().isBlank() ? "" : request.companyDescription());
    }
    if (request.address() != null) {
        company.setAddress(request.address().isBlank() ? "" : request.address());
    }
    
    if (request.industryType() != null) {
        company.setIndustryType(request.industryType());
    }
    
    if (request.websiteUrl() != null) {
        company.setWebsiteUrl(request.websiteUrl().isBlank() ? "" : request.websiteUrl());
    }
    if (request.logoImagePath() != null) {
        company.setLogoImagePath(request.logoImagePath().isBlank() ? null : request.logoImagePath());
    }
    if (request.linkedinUrl() != null) {
        company.setLinkedinUrl(request.linkedinUrl().isBlank() ? "" : request.linkedinUrl());
    }
    if (request.youtubeUrl() != null) {
        company.setYoutubeUrl(request.youtubeUrl().isBlank() ? "" : request.youtubeUrl());
    }
    if (request.instagramUrl() != null) {
        company.setInstagramUrl(request.instagramUrl().isBlank() ? "" : request.instagramUrl());
    }
    if (request.twitterUrl() != null) {
        company.setTwitterUrl(request.twitterUrl().isBlank() ? "" : request.twitterUrl());
    }
    
    if (request.agreeToTerms() != null) {
        company.setAgreeToTerms(request.agreeToTerms());
    }

        companyRepository.save(company);

        logService.log(
            username,
            company.getRole().name(),
            "UPDATE_PROFILE",
            "Updated company profile"
        );
    }


    public CompanyProfileResponse getMyProfile(String username) {
        Company company = companyRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
    
        CompanyProfileResponse response = CompanyProfileResponse.builder()
            .name(company.getCompanyName())
            .description(company.getCompanyDescription())
            .address(company.getAddress())
            .industryType(company.getIndustryType() != null ? company.getIndustryType().name() : null)  
            .websiteUrl(company.getWebsiteUrl())
            .linkedinUrl(company.getLinkedinUrl())
            .youtubeUrl(company.getYoutubeUrl())
            .instagramUrl(company.getInstagramUrl())
            .twitterUrl(company.getTwitterUrl())
            .logoImgPath(company.getLogoImagePath())
            .build();
    
        logService.log(
            username,
            company.getRole().name(),
            "VIEW_PROFILE",
            "Viewed company profile"
        );
    
        return response;
    }

    public CompanyProfileResponse createProfile(String username, CompanyProfileRequest request) {
        Company company = companyRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        company.setCompanyName(request.companyName() != null && !request.companyName().isBlank() ? request.companyName() : "");
        company.setCompanyDescription(request.companyDescription() != null && !request.companyDescription().isBlank() ? request.companyDescription() : "");
        company.setAddress(request.address() != null && !request.address().isBlank() ? request.address() : "");
        
        if (request.industryType() != null) {
            company.setIndustryType(request.industryType());
        }
        
        company.setWebsiteUrl(request.websiteUrl() != null && !request.websiteUrl().isBlank() ? request.websiteUrl() : "");
        company.setLogoImagePath(request.logoImagePath() != null && !request.logoImagePath().isBlank() ? request.logoImagePath() : null);
        company.setLinkedinUrl(request.linkedinUrl() != null && !request.linkedinUrl().isBlank() ? request.linkedinUrl() : "");
        company.setYoutubeUrl(request.youtubeUrl() != null && !request.youtubeUrl().isBlank() ? request.youtubeUrl() : "");
        company.setInstagramUrl(request.instagramUrl() != null && !request.instagramUrl().isBlank() ? request.instagramUrl() : "");
        company.setTwitterUrl(request.twitterUrl() != null && !request.twitterUrl().isBlank() ? request.twitterUrl() : "");
        
        if (request.agreeToTerms() != null) {
            company.setAgreeToTerms(request.agreeToTerms());
        }
        
        company.setProfileCompleted(true);

        companyRepository.save(company);

        logService.log(
            username,
            company.getRole().name(),
            "CREATE_PROFILE",
            "Created company profile"
        );

        return CompanyProfileResponse.builder()
                .name(company.getCompanyName())
                .description(company.getCompanyDescription())
                .address(company.getAddress())
                .industryType(company.getIndustryType() != null ? company.getIndustryType().name() : null)
                .websiteUrl(company.getWebsiteUrl())
                .linkedinUrl(company.getLinkedinUrl())
                .youtubeUrl(company.getYoutubeUrl())
                .instagramUrl(company.getInstagramUrl())
                .twitterUrl(company.getTwitterUrl())
                .logoImgPath(company.getLogoImagePath())
                .build();
    }

    private String generateUsername(String username) {
        String newUsername;
        do {
            String baseName = username.split("@")[0];
            String uniqueId = RandomStringUtils.secure().next(6, true, true);
            newUsername = baseName + "_hr_" + uniqueId;
        } while (userRepository.existsByUsername(newUsername));
        return newUsername;
    }

    private String generatePassword() {
        String upperCaseLetters = RandomStringUtils.secure().next(4, true, false).toUpperCase();
        String alphanumerics = RandomStringUtils.secure().next(8, true, true);
        String digits = RandomStringUtils.secure().next(4, false, true);
        return upperCaseLetters + alphanumerics + digits;
    }
}
