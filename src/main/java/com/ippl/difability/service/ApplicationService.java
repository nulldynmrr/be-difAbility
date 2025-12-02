package com.ippl.difability.service;

import org.springframework.stereotype.Service;

import com.ippl.difability.dto.request.ApplicationRequest;
import com.ippl.difability.dto.request.ApplicationReviewRequest;
import com.ippl.difability.entity.Application;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.entity.Job;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.enums.ApplicationStatus;
import com.ippl.difability.enums.PublicationStatus;
import com.ippl.difability.exception.ApplicationNotFoundException;
import com.ippl.difability.exception.DuplicateApplicationException;
import com.ippl.difability.exception.ForbiddenException;
import com.ippl.difability.exception.JobNotFoundException;
import com.ippl.difability.exception.UserNotFoundException;
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
    private final JobSeekerRepository jobSeekerRepository;
    private final JobRepository jobRepository;
    private final HumanResourceRepository humanResourceRepository;
    private final ApplicationRepository applicationRepository;
    private final LogService logService;

    public void createApplication(String username, Long jobId, ApplicationRequest request){
        JobSeeker jobSeeker = jobSeekerRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        Job job = jobRepository.findById(jobId)
            .orElseThrow(JobNotFoundException::new);

        if(applicationRepository.existsByJobSeekerAndJob(jobSeeker, job)){
            throw new DuplicateApplicationException();
        }

        Application application = new Application();
        application.setJobSeeker(jobSeeker);
        application.setJob(job);
        application.setCoverLetter(request.coverLetter());
        applicationRepository.save(application);

        logService.log(
            username,
            jobSeeker.getRole().name(),
            "CREATE_APPLICATION",
            "Created Application." 
        );
    }

    public void reviewApplication(String username, Long applicationId, ApplicationReviewRequest request){
        HumanResource humanResource = humanResourceRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(ApplicationNotFoundException::new);
        
        Job job = application.getJob();

        if(!job.getCompany().getId().equals(humanResource.getCompany().getId())){
            throw new ForbiddenException();
        }

        application.setStatus(request.status());
        application.setHrNotes(request.hrNotes());

        if(application.getStatus() == ApplicationStatus.ACCEPTED){
            job.setPublicationStatus(PublicationStatus.CLOSED);
        }
        
        logService.log(
            username,
            humanResource.getRole().name(),
            "REVIEW_APPLICATION",
            "Reviewed Application." 
        );
    }
}
