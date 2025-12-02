package com.ippl.difability.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import com.ippl.difability.dto.request.CompanyProfileRequest;
import com.ippl.difability.dto.request.HumanResourceProfileRequest;
import com.ippl.difability.dto.request.JobSeekerProfileRequest;
import com.ippl.difability.dto.response.UserResponse;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.entity.User;
import com.ippl.difability.exception.IncompleteRequestException;
import com.ippl.difability.exception.UserNotFoundException;
import com.ippl.difability.repository.CompanyRepository;
import com.ippl.difability.repository.HumanResourceRepository;
import com.ippl.difability.repository.JobSeekerRepository;
import com.ippl.difability.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final LogService logService;
    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final CompanyRepository companyRepository;
    private final HumanResourceRepository humanResourceRepository;
    
    public List<UserResponse> getAllUsers(){
        return userRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public void updateJobSeekerProfile(String username, JobSeekerProfileRequest request){
        JobSeeker jobSeeker = jobSeekerRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
        validateJobSeekerInput(jobSeeker, request);
        jobSeekerRepository.save(jobSeeker);

        logService.log(
            username,
            jobSeeker.getRole().name(),
            "UPDATE_PROFILE",
            username + " updated their profile."
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
            "UPDATE_PROFILE",
            username + " updated their profile."
        );
    }

    public void updateHumanResourceProfile(String username, HumanResourceProfileRequest request){
        HumanResource humanResource = humanResourceRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
        validateHumanResourceInput(humanResource, request);
        humanResourceRepository.save(humanResource);

        logService.log(
            username,
            humanResource.getRole().name(),
            "UPDATE_PROFILE",
            username + " updated their profile."
        );
    }

    private UserResponse mapToResponse(User user){
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.isProfileCompleted(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
    private void validateJobSeekerInput(JobSeeker jobSeeker, JobSeekerProfileRequest request){
        if(!jobSeeker.isProfileCompleted()){
            if(request.fullName() == null
                || request.about() == null
                || request.address() == null
                || request.disabilityType() == null
                || request.skills() == null
                || request.educationLevel() == null
                || request.ppImagePath() == null
                || request.cvDocumentPath() == null){
                throw new IncompleteRequestException("Missing required fields.");
            }
            jobSeeker.setFullName(request.fullName());
            jobSeeker.setAbout(request.about());
            jobSeeker.setAddress(request.address());
            jobSeeker.setDisabilityType(request.disabilityType());
            jobSeeker.setSkills(request.skills());
            jobSeeker.setEducationLevel(request.educationLevel());
            jobSeeker.setPpImagePath(request.ppImagePath());
            jobSeeker.setCvDocumentPath(request.cvDocumentPath());
            jobSeeker.setCertificationFilePaths(request.certificationFilePaths());
            jobSeeker.setProfileCompleted(true);
            return;
        }
        if(request.fullName() != null) jobSeeker.setFullName(request.fullName());
        if(request.about() != null) jobSeeker.setAbout(request.about());
        if(request.address() != null) jobSeeker.setAddress(request.address());
        if(request.disabilityType() != null) jobSeeker.setDisabilityType(request.disabilityType());
        if(request.skills() != null) jobSeeker.setSkills(request.skills());
        if(request.educationLevel() != null) jobSeeker.setEducationLevel(request.educationLevel());
        if(request.ppImagePath() != null) jobSeeker.setPpImagePath(request.ppImagePath());
        if(request.cvDocumentPath() != null) jobSeeker.setCvDocumentPath(request.cvDocumentPath());
        if(request.certificationFilePaths() != null) jobSeeker.setCertificationFilePaths(request.certificationFilePaths());
    }

    private void validateCompanyInput(Company company, CompanyProfileRequest request){
        if(!company.isProfileCompleted()){
            if(request.companyName() == null
                    || request.companyDescription() == null
                    || request.address() == null
                    || request.industryType() == null
                    || request.websiteUrl() == null
                    || request.logoImagePath() == null){
                throw new IncompleteRequestException("Missing required fields.");
            }
            company.setCompanyName(request.companyName());
            company.setCompanyDescription(request.companyDescription());
            company.setAddress(request.address());
            company.setIndustryType(request.industryType());
            company.setWebsiteUrl(request.websiteUrl());
            company.setLogoImagePath(request.logoImagePath());
            company.setProfileCompleted(true);
            return;
        }
        if(request.companyName() != null) company.setCompanyName(request.companyName());
        if(request.companyDescription() != null) company.setCompanyDescription(request.companyDescription());
        if(request.address() != null) company.setAddress(request.address());
        if(request.industryType() != null) company.setIndustryType(request.industryType());
        if(request.websiteUrl() != null) company.setWebsiteUrl(request.websiteUrl());
        if(request.logoImagePath() != null) company.setLogoImagePath(request.logoImagePath());
    }

    private void validateHumanResourceInput(HumanResource humanResource, HumanResourceProfileRequest request){
        if(!humanResource.isProfileCompleted()){
            if(request.fullName() == null || request.contact() == null){
                throw new IncompleteRequestException("Missing required fields.");
            }
            humanResource.setFullName(request.fullName());
            humanResource.setContact(request.contact());
            return;
        }
        if(request.fullName() != null) humanResource.setFullName(request.fullName());
        if(request.contact() != null) humanResource.setContact(request.contact());
    }
}