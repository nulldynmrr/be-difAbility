package com.ippl.difability.service;

import org.springframework.stereotype.Service;

import com.ippl.difability.dto.ReviewApplicationRequest;
import com.ippl.difability.dto.ReviewApplicationResponse;
import com.ippl.difability.entity.Application;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.entity.Job;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.enums.ApplicationStatus;
import com.ippl.difability.enums.PublicationStatus;
import com.ippl.difability.exception.ForbiddenException;
import com.ippl.difability.exception.ResourceConflictException;
import com.ippl.difability.exception.ResourceNotFoundException;
import com.ippl.difability.repository.ApplicationRepository;
import com.ippl.difability.repository.HumanResourceRepository;
import com.ippl.difability.repository.JobRepository;
import com.ippl.difability.repository.JobSeekerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {
    private final ActivityLogService activityLogService;
    private final ApplicationRepository applicationRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final HumanResourceRepository humanResourceRepository;
    private final JobRepository jobRepository;

    public Application createApplication(String identifier, Long jobId){
        JobSeeker jobSeeker = jobSeekerRepository.findByIdentifier(identifier)
            .orElseThrow(() -> new ResourceNotFoundException("Job Seeker not found."));

        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job not found."));

        if(applicationRepository.existsByJobSeekerAndJob(jobSeeker, job)){
            throw new ResourceConflictException(
                jobSeeker.getIdentifier() + " has already applied to " + job.getTitle()
            );
        }
        
        Application application = new Application();
        application.setJobSeeker(jobSeeker);
        application.setJob(job);

        activityLogService.log(
            jobSeeker.getIdentifier(),
            jobSeeker.getRole().name(),
            "CREATE_APPLICATION",
            jobSeeker.getRole() + " applied to: " + job.getTitle()
        );

        return applicationRepository.save(application);
    }
    
    public ReviewApplicationResponse reviewApplication(String identifier, Long applicationId, ReviewApplicationRequest request){
        HumanResource humanResource = humanResourceRepository.findByIdentifier(identifier)
            .orElseThrow(() -> new ResourceNotFoundException("Human Resource not found."));
        
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Application not found."));

        Job job = application.getJob();

        if(!job.getCompany().equals(humanResource.getCompany())){
            throw new ForbiddenException("HR is not from company " + job.getCompany().getName());
        }
        
        if(application.getStatus() != ApplicationStatus.UNDER_REVIEW){
            throw new ResourceConflictException("Application already reviewed.");
        }
        
        application.setStatus(request.getStatus());
        application.setHrNotes(request.getHrNotes());

        if(application.getStatus() == ApplicationStatus.ACCEPTED){
            job.setPublicationStatus(PublicationStatus.CLOSED);
            jobRepository.save(job);
        }

        activityLogService.log(
            humanResource.getIdentifier(),
            humanResource.getRole().name(),
            "REVIEW_APPLICATION",
            humanResource.getRole().name() + " reviewed application: " + applicationId
        );

        applicationRepository.save(application);
        return new ReviewApplicationResponse(
            job.getTitle(),
            application.getJobSeeker().getName(),
            application.getStatus().getLabel(),
            application.getHrNotes(),
            application.getAppliedAt()
        );
    }
}
