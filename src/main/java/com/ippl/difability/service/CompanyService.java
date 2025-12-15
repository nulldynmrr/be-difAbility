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

    company.setCompanyName(isValidField(request.companyName()) ? request.companyName() : null);
    company.setCompanyDescription(isValidField(request.companyDescription()) ? request.companyDescription() : null);
    company.setAddress(isValidField(request.address()) ? request.address() : null);
    
    if (request.industryType() != null) {
        company.setIndustryType(request.industryType());
    }
    
    company.setWebsiteUrl(isValidField(request.websiteUrl()) ? request.websiteUrl() : null);
    company.setLogoImagePath(isValidField(request.logoImagePath()) ? request.logoImagePath() : null);
    company.setLinkedinUrl(isValidField(request.linkedinUrl()) ? request.linkedinUrl() : null);
    company.setYoutubeUrl(isValidField(request.youtubeUrl()) ? request.youtubeUrl() : null);
    company.setInstagramUrl(isValidField(request.instagramUrl()) ? request.instagramUrl() : null);
    company.setTwitterUrl(isValidField(request.twitterUrl()) ? request.twitterUrl() : null);
    
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


    private boolean isValidField(String field) {
        return field != null && !field.isBlank() && !field.equals("-");
    }

    public CompanyProfileResponse getMyProfile(String username) {
        Company company = companyRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
    
        // Normalize "-" values to null when reading from database
        String companyName = normalizeField(company.getCompanyName());
        String companyDescription = normalizeField(company.getCompanyDescription());
    
        CompanyProfileResponse response = CompanyProfileResponse.builder()
            .name(companyName)
            .description(companyDescription)
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

    private String normalizeField(String field) {
        if (field == null || field.isBlank() || field.equals("-")) {
            return null;
        }
        return field;
    }

    public CompanyProfileResponse createProfile(String username, CompanyProfileRequest request) {
        Company company = companyRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        company.setCompanyName(isValidField(request.companyName()) ? request.companyName() : null);
        company.setCompanyDescription(isValidField(request.companyDescription()) ? request.companyDescription() : null);
        company.setAddress(isValidField(request.address()) ? request.address() : null);
        
        if (request.industryType() != null) {
            company.setIndustryType(request.industryType());
        }
        
        company.setWebsiteUrl(isValidField(request.websiteUrl()) ? request.websiteUrl() : null);
        company.setLogoImagePath(isValidField(request.logoImagePath()) ? request.logoImagePath() : null);
        company.setLinkedinUrl(isValidField(request.linkedinUrl()) ? request.linkedinUrl() : null);
        company.setYoutubeUrl(isValidField(request.youtubeUrl()) ? request.youtubeUrl() : null);
        company.setInstagramUrl(isValidField(request.instagramUrl()) ? request.instagramUrl() : null);
        company.setTwitterUrl(isValidField(request.twitterUrl()) ? request.twitterUrl() : null);
        
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

        // Normalize "-" values to null when returning response
        String companyName = normalizeField(company.getCompanyName());
        String companyDescription = normalizeField(company.getCompanyDescription());

        return CompanyProfileResponse.builder()
                .name(companyName)
                .description(companyDescription)
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
