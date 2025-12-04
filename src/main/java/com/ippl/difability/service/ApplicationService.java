package com.ippl.difability.service;

import org.springframework.stereotype.Service;

import com.ippl.difability.dto.request.ApplicationRequest;
import com.ippl.difability.dto.request.ApplicationReviewRequest;
import com.ippl.difability.dto.response.ApplicationResponse;
import com.ippl.difability.entity.Application;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.entity.Job;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.enums.ApplicationStatus;
import com.ippl.difability.enums.PublicationStatus;
import com.ippl.difability.exception.ApplicationNotFoundException;
import com.ippl.difability.exception.ApplicationReviewedException;
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

    // cek hr, job, application
    // cek hr.company == job.company
    // cek application.job == job
    public ApplicationResponse getApplication(String username, Long jobId, Long applicationId){
        HumanResource humanResource = humanResourceRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        Job job = jobRepository.findById(jobId)
            .orElseThrow(JobNotFoundException::new);

        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(ApplicationNotFoundException::new);

        if(!humanResource.getCompany().getId().equals(job.getCompany().getId())){
            throw new ForbiddenException();
        }
        
        if(!application.getJob().getId().equals(job.getId())){
            throw new ForbiddenException();
        }

        logService.log(
            username,
            humanResource.getRole().name(),
            "VIEW_APPLICATION"
        );

        return mapToResponse(application);
    }

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
            "CREATE_APPLICATION"
        );
    }

    public void reviewApplication(String username, Long jobId, Long applicationId, ApplicationReviewRequest request){
        HumanResource humanResource = humanResourceRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        Job job = jobRepository.findById(jobId)
            .orElseThrow(JobNotFoundException::new);

        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(ApplicationNotFoundException::new);

        if(application.getStatus() != ApplicationStatus.UNDER_REVIEW){
            throw new ApplicationReviewedException();
        }
        
        if(!humanResource.getCompany().getId().equals(job.getCompany().getId())){
            throw new ForbiddenException();
        }

        if(!application.getJob().getId().equals(job.getId())){
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
            "REVIEW_APPLICATION"
        );
    }

    public void deleteApplication(String username, Long applicationId){
        JobSeeker jobSeeker = jobSeekerRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
        
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(ApplicationNotFoundException::new);
        
        if(!jobSeeker.getId().equals(application.getJobSeeker().getId())){
            throw new ForbiddenException();
        }

        applicationRepository.delete(application);

        logService.log(
            username,
            jobSeeker.getRole().name(),
            "DELETE_APPLICATION"
        );
    }

    private ApplicationResponse mapToResponse(Application application){ 
        return new ApplicationResponse(
            application.getId(),
            application.getJob().getId(),
            application.getJobSeeker().getId(),
            application.getJobSeeker().getCvDocumentPath(),
            application.getCoverLetter(),
            application.getAppliedAt()
        );
    }
}
