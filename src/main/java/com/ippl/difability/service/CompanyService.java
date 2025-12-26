package com.ippl.difability.service;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ippl.difability.dto.request.CompanyProfileRequest;
import com.ippl.difability.dto.response.HrCredentialResponse;
import com.ippl.difability.dto.response.user.CompanyDetailResponse;
import com.ippl.difability.dto.response.user.HumanResourceDetailResponse;
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

    public CompanyDetailResponse getCompanyProfile(Long id){
        Company company = companyRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        return new CompanyDetailResponse(
            company.getCompanyName(),
            company.getCompanyDescription(),
            company.getAddress(),
            company.getIndustryType(),
            company.getWebsiteUrl(),
            company.getLinkedinUrl(),
            company.getYoutubeUrl(),
            company.getInstagramUrl(),
            company.getTwitterUrl(),
            company.getLogoImagePath()
        );
    }

    public CompanyDetailResponse getMyProfile(String username){
        Company company = companyRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        return new CompanyDetailResponse(
            company.getCompanyName(),
            company.getCompanyDescription(),
            company.getAddress(),
            company.getIndustryType(),
            company.getWebsiteUrl(),
            company.getLinkedinUrl(),
            company.getYoutubeUrl(),
            company.getInstagramUrl(),
            company.getTwitterUrl(),
            company.getLogoImagePath()
        );
    }

    public void updateCompanyProfile(String username, CompanyProfileRequest request){
        Company company = companyRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
        validateCompanyInput(company, request);
        companyRepository.save(company);

        logService.log(
            username,
            company.getRole().name(),
            "UPDATE_PROFILE"
        );
    }

    public HrCredentialResponse generateHrAccount(String username){
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
            "CREATE_HR"
        );

        return new HrCredentialResponse(newUsername, newPassword);
    }
    
    public List<HumanResourceDetailResponse> getHumanResources(String username){
        Company company = companyRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        return humanResourceRepository.findAllByCompanyId(company.getId()).stream()
            .map(humanResource -> new HumanResourceDetailResponse(
                humanResource.getFullName(),
                humanResource.getContact(),
                humanResource.getPpImagePath()
            )).toList();
    }

    private String generateUsername(String username){
        boolean usernameExists;
        String newUsername;

        do{
            String baseName = username.split("@")[0];
            String uniqueId = RandomStringUtils.secure().next(6, true, true);
            newUsername = baseName + "_hr_" + uniqueId;
            usernameExists = !userRepository.existsByUsername(username);
        }while(usernameExists);

        return newUsername;
    }
    
    private String generatePassword(){
        String upperCaseLetters = RandomStringUtils.secure().next(4, true, false).toUpperCase();
        String alphanumerics = RandomStringUtils.secure().next(8, true, true);
        String digits = RandomStringUtils.secure().next(4, false, true);
        
        return upperCaseLetters + alphanumerics + digits;
    }

    private void validateCompanyInput(Company company, CompanyProfileRequest request){
        // if(!company.isProfileCompleted()){
        //     if(request.companyName() == null
        //             || request.companyDescription() == null
        //             || request.address() == null
        //             || request.industryType() == null
        //             || request.logoImagePath() == null
        //             || !request.agreeToTerms()){
        //         throw new IncompleteRequestException("Missing required fields.");
        //     }
        //     company.setCompanyName(request.companyName());
        //     company.setCompanyDescription(request.companyDescription());
        //     company.setAddress(request.address());
        //     company.setIndustryType(request.industryType());
        //     company.setWebsiteUrl(request.websiteUrl());
        //     company.setLinkedinUrl(request.linkedinUrl());
        //     company.setYoutubeUrl(request.youtubeUrl());
        //     company.setInstagramUrl(request.instagramUrl());
        //     company.setTwitterUrl(request.twitterUrl());
        //     company.setLogoImagePath(request.logoImagePath());
        //     company.setAgreeToTerms(request.agreeToTerms());
        //     company.setProfileCompleted(true);
        //     return;
        // }
        if(request.companyName() != null) company.setCompanyName(request.companyName());
        if(request.companyDescription() != null) company.setCompanyDescription(request.companyDescription());
        if(request.address() != null) company.setAddress(request.address());
        if(request.industryType() != null) company.setIndustryType(request.industryType());
        if(request.websiteUrl() != null) company.setWebsiteUrl(request.websiteUrl());
        if(request.linkedinUrl() != null) company.setLinkedinUrl(request.linkedinUrl());
        if(request.youtubeUrl() != null) company.setYoutubeUrl(request.youtubeUrl());
        if(request.instagramUrl() != null) company.setInstagramUrl(request.instagramUrl());
        if(request.twitterUrl() != null) company.setTwitterUrl(request.twitterUrl());
        if(request.logoImagePath() != null) company.setLogoImagePath(request.logoImagePath());
        if(request.agreeToTerms() != null) company.setAgreeToTerms(request.agreeToTerms());
    }
}