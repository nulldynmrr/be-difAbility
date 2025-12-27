package com.ippl.difability.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ippl.difability.dto.request.JobSeekerProfileRequest;
import com.ippl.difability.dto.response.user.JobSeekerDetailResponse;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.exception.IncompleteRequestException;
import com.ippl.difability.exception.UserNotFoundException;
import com.ippl.difability.repository.JobSeekerRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class JobSeekerService {
    private final JobSeekerRepository jobSeekerRepository;
    private final LogService logService;

    public JobSeekerDetailResponse getJobSeekerProfile(Long id){
        JobSeeker jobSeeker = jobSeekerRepository.findById(id)
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

    public JobSeekerDetailResponse getMyProfile(String username){
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
    
    public void updateJobSeekerProfile(String username, JobSeekerProfileRequest request){
        JobSeeker jobSeeker = jobSeekerRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
            
        validateJobSeekerInput(jobSeeker, request);
        jobSeekerRepository.save(jobSeeker);

        logService.log(
            username,
            jobSeeker.getRole().name(),
            "UPDATE_PROFILE",
            "Memperbarui profil Job Seeker: " + jobSeeker.getFullName()
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
}
