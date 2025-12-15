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

    if (isValidField(request.companyName())) {
        company.setCompanyName(request.companyName());
    }
    if (isValidField(request.companyDescription())) {
        company.setCompanyDescription(request.companyDescription());
    }
    if (isValidField(request.address())) {
        company.setAddress(request.address());
    }
    if (request.industryType() != null) {
        company.setIndustryType(request.industryType());
    }
    if (isValidField(request.websiteUrl())) {
        company.setWebsiteUrl(request.websiteUrl());
    }
    if (isValidField(request.logoImagePath())) {
        company.setLogoImagePath(request.logoImagePath());
    }
    if (isValidField(request.linkedinUrl())) {
        company.setLinkedinUrl(request.linkedinUrl());
    }
    if (isValidField(request.youtubeUrl())) {
        company.setYoutubeUrl(request.youtubeUrl());
    }
    if (isValidField(request.instagramUrl())) {
        company.setInstagramUrl(request.instagramUrl());
    }
    if (isValidField(request.twitterUrl())) {
        company.setTwitterUrl(request.twitterUrl());
    }

        companyRepository.save(company);

        logService.log(
            username,
            company.getRole().name(),
            "UPDATE_PROFILE",
            "Updated company profile"
        );
    }


    private boolean isValidField(String field) {
        return field != null && !field.isBlank() && !field.equals("-");
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
                .orElse(new Company());

        company.setUsername(username); 
        company.setCompanyName(request.companyName());
        company.setCompanyDescription(request.companyDescription());
        company.setAddress(request.address());
        company.setIndustryType(request.industryType());
        company.setWebsiteUrl(request.websiteUrl());
        company.setLogoImagePath(request.logoImagePath());
        company.setLinkedinUrl(request.linkedinUrl());
        company.setYoutubeUrl(request.youtubeUrl());
        company.setInstagramUrl(request.instagramUrl());
        company.setTwitterUrl(request.twitterUrl());

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
        boolean usernameExists;
        String newUsername;
        do {
            String baseName = username.split("@")[0];
            String uniqueId = RandomStringUtils.secure().next(6, true, true);
            newUsername = baseName + "_hr_" + uniqueId;
            usernameExists = !userRepository.existsByUsername(newUsername);
        } while (usernameExists);
        return newUsername;
    }

    private String generatePassword() {
        String upperCaseLetters = RandomStringUtils.secure().next(4, true, false).toUpperCase();
        String alphanumerics = RandomStringUtils.secure().next(8, true, true);
        String digits = RandomStringUtils.secure().next(4, false, true);
        return upperCaseLetters + alphanumerics + digits;
    }
}
