package com.ippl.difability.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ippl.difability.dto.request.CompanyProfileRequest;
import com.ippl.difability.dto.request.HumanResourceProfileRequest;
import com.ippl.difability.dto.request.JobSeekerProfileRequest;
import com.ippl.difability.dto.response.user.CompanyDetailResponse;
import com.ippl.difability.dto.response.user.HumanResourceDetailResponse;
import com.ippl.difability.dto.response.user.JobSeekerDetailResponse;
import com.ippl.difability.dto.response.user.UserBaseResponse;
import com.ippl.difability.dto.response.user.UserDetailsResponse;
import com.ippl.difability.dto.response.user.UserResponse;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.entity.User;
import com.ippl.difability.enums.Role;
import com.ippl.difability.exception.ForbiddenException;
import com.ippl.difability.exception.IncompleteRequestException;
import com.ippl.difability.exception.UserNotFoundException;
import com.ippl.difability.repository.ApplicationRepository;
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
    private final ApplicationRepository applicationRepository;
    
    public List<UserResponse> getUsers(){
        return userRepository.findAllByOrderByCreatedAtDesc()
            .stream()
            .map(user -> new UserResponse(
                buildBase(user),
                buildDetails(user)
            ))
            .collect(Collectors.toList());
    }

    public void deleteUser(String username, Long userId){
        User admin = userRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
        
        logService.log(
            username,
            admin.getRole().name(),
            "DELETE_USER"
        );
    }

    public UserResponse getUser(String requesterUsername, Long targetId){
        User requester = userRepository.findByUsername(requesterUsername)
            .orElseThrow(UserNotFoundException::new);

        Role requesterRole = requester.getRole();
        
        User targetUser = userRepository.findById(targetId)
            .orElseThrow(UserNotFoundException::new);
        
        UserBaseResponse base = null;
        if(targetUser instanceof Company company){
            UserDetailsResponse details = buildDetails(company);
            return new UserResponse(base, details);
        }
        UserDetailsResponse details;

        switch(requesterRole){
            case ADMIN ->{
                base = buildBase(targetUser);
                details = buildDetails(targetUser);
            }
            case HUMAN_RESOURCE ->{
                HumanResource tempHr = (HumanResource) requester;

                boolean hasRelationship = applicationRepository.existsByJobSeekerIdAndJobCompanyId(
                    targetUser.getId(),
                    tempHr.getCompany().getId()
                );

                if(!hasRelationship){
                    throw new ForbiddenException();
                }
                details = buildDetails(targetUser);
            }
            default ->
                throw new ForbiddenException();
        }
        return new UserResponse(base, details);
    }

    public JobSeekerDetailResponse getProfileJobSeeker(String username){
        JobSeeker jobSeeker = jobSeekerRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        return new JobSeekerDetailResponse(
            jobSeeker.getFullName(),
            jobSeeker.getAbout(),
            jobSeeker.getAddress(),
            jobSeeker.getDisabilityType(),
            jobSeeker.getSkills(),
            jobSeeker.getCertificationFilePaths(),
            jobSeeker.getEducationLevel(),
            jobSeeker.getAcademicYear(),
            jobSeeker.getJobType(),
            jobSeeker.getPpImagePath(),
            jobSeeker.getCvDocumentPath()
        );
    }

    public CompanyDetailResponse getProfileCompany(String username){
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
    
    
    public void updateJobSeekerProfile(String username, JobSeekerProfileRequest request){
        JobSeeker jobSeeker = jobSeekerRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
            
        validateJobSeekerInput(jobSeeker, request);
        jobSeekerRepository.save(jobSeeker);

        logService.log(
            username,
            jobSeeker.getRole().name(),
            "UPDATE_PROFILE"
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

    public void updateHumanResourceProfile(String username, HumanResourceProfileRequest request){
        HumanResource humanResource = humanResourceRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
        validateHumanResourceInput(humanResource, request);
        humanResourceRepository.save(humanResource);

        logService.log(
            username,
            humanResource.getRole().name(),
            "UPDATE_PROFILE"
        );
    }

    private UserBaseResponse buildBase(User user){
        return new UserBaseResponse(
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.isProfileCompleted(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }

    private UserDetailsResponse buildDetails(User user) {
        return switch (user.getRole()) {
            case ADMIN ->
                null;
            case JOB_SEEKER ->{
                JobSeeker jobSeeker = (JobSeeker) user;
                yield new JobSeekerDetailResponse(
                    jobSeeker.getFullName(),
                    jobSeeker.getAbout(),
                    jobSeeker.getAddress(),
                    jobSeeker.getDisabilityType(),
                    jobSeeker.getSkills(),
                    jobSeeker.getCertificationFilePaths(),
                    jobSeeker.getEducationLevel(),
                    jobSeeker.getAcademicYear(),
                    jobSeeker.getJobType(),
                    jobSeeker.getPpImagePath(),
                    jobSeeker.getCvDocumentPath()
                );
            }
            case HUMAN_RESOURCE ->{
                HumanResource humanResource = (HumanResource) user;
                yield new HumanResourceDetailResponse(
                    humanResource.getFullName(),
                    humanResource.getContact(),
                    humanResource.getPpImagePath()
                );
            }
            case COMPANY ->{
                Company company = (Company) user;
                yield new CompanyDetailResponse(
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
            default -> 
                throw new ForbiddenException();
        };
    }

    
    private void validateJobSeekerInput(JobSeeker jobSeeker, JobSeekerProfileRequest request){
        if(!jobSeeker.isProfileCompleted()){
            if(request.fullName() == null
                || request.about() == null
                || request.address() == null
                || request.disabilityType() == null
                || request.skills() == null
                || request.educationLevel() == null
                || request.academicYear() == null
                || request.jobType() == null
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
            jobSeeker.setAcademicYear(request.academicYear());
            jobSeeker.setJobType(request.jobType());
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
        if(request.academicYear() != null) jobSeeker.setAcademicYear(request.academicYear());
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
                    || request.logoImagePath() == null
                    || !request.agreeToTerms()){
                throw new IncompleteRequestException("Missing required fields.");
            }
            company.setCompanyName(request.companyName());
            company.setCompanyDescription(request.companyDescription());
            company.setAddress(request.address());
            company.setIndustryType(request.industryType());
            company.setWebsiteUrl(request.websiteUrl());
            company.setLinkedinUrl(request.linkedinUrl());
            company.setYoutubeUrl(request.youtubeUrl());
            company.setInstagramUrl(request.instagramUrl());
            company.setTwitterUrl(request.twitterUrl());
            company.setLogoImagePath(request.logoImagePath());
            company.setAgreeToTerms(request.agreeToTerms());
            company.setProfileCompleted(true);
            return;
        }
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