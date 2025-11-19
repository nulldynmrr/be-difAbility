package com.ippl.difability.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ippl.difability.dto.UpdateCompanyProfileRequest;
import com.ippl.difability.dto.UpdateJobSeekerProfileRequest;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.enums.Role;
import com.ippl.difability.exception.ForbiddenException;
import com.ippl.difability.exception.ResourceNotFoundException;
import com.ippl.difability.repository.CompanyRepository;
import com.ippl.difability.repository.JobSeekerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final CompanyRepository companyRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final ActivityLogService activityLogService;

    public JobSeeker updateJobSeekerProfile(String identifier, UpdateJobSeekerProfileRequest request){ 
        JobSeeker jobSeeker = jobSeekerRepository.findByIdentifier(identifier)
            .orElseThrow(() -> new ResourceNotFoundException("Job Seeker not found."));

        if(jobSeeker.getRole() != Role.JOB_SEEKER){
            throw new ForbiddenException("Only Job Seekers can update this profile type.");
        }

        applyJobSeekerUpdates(jobSeeker, request);
        activityLogService.log(
            jobSeeker.getIdentifier(),
            jobSeeker.getRole().name(),
            "UPDATE_PROFILE",
            jobSeeker.getRole().name() + " updated their profile"
        );

        return jobSeekerRepository.save(jobSeeker);
    }

    public Company updateCompanyProfile(String identifier, UpdateCompanyProfileRequest request){ 
        Company company = companyRepository.findByIdentifier(identifier)
            .orElseThrow(() -> new ResourceNotFoundException("Company not found."));
            
        if(company.getRole() != Role.COMPANY){
            throw new ForbiddenException("Only Companies can update this profile type.");
        }
        applyCompanyUpdates(company, request);
        
        activityLogService.log(
            company.getIdentifier(),
            company.getRole().name(),
            "UPDATE_PROFILE",
            company.getRole().name() + " updated their profile"
        );

        return companyRepository.save(company);
    }
    private void applyJobSeekerUpdates(JobSeeker jobSeeker, UpdateJobSeekerProfileRequest request){
        if (request.getName() != null) jobSeeker.setName(request.getName());
        if (request.getAbout() != null) jobSeeker.setAbout(request.getAbout());
        if (request.getAddress() != null) jobSeeker.setAddress(request.getAddress());
        if (request.getDisabilityType() != null) jobSeeker.setDisabilityType(request.getDisabilityType());
        if (request.getSkills() != null) jobSeeker.setSkills(request.getSkills());
        if (request.getEducationLevel() != null) jobSeeker.setEducationLevel(request.getEducationLevel());
        if (request.getPpImgPath() != null) jobSeeker.setPpImgPath(request.getPpImgPath());
        if (request.getCvFilePath() != null) jobSeeker.setCvFilePath(request.getCvFilePath());
        if (request.getCertifFilePaths() != null) jobSeeker.setCertifFilePaths(request.getCertifFilePaths());
        jobSeeker.setProfileCompleted(true);
    }

    private void applyCompanyUpdates(Company company, UpdateCompanyProfileRequest request){
        if (request.getName() != null) company.setName(request.getName());
        if (request.getDescription() != null) company.setDescription(request.getDescription());
        if (request.getAddress() != null) company.setAddress(request.getAddress());
        if (request.getIndustryType() != null) company.setIndustryType(request.getIndustryType());
        if (request.getWebsiteUrl() != null) company.setWebsiteUrl(request.getWebsiteUrl());
        if (request.getLogoImgPath() != null) company.setLogoImgPath(request.getLogoImgPath());
        company.setProfileCompleted(true);
    }
}

