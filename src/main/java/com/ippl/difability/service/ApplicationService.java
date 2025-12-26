package com.ippl.difability.service;

import java.util.List;

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

    public List<ApplicationResponse> getApplications(String username, Long jobId){
        HumanResource humanResource = humanResourceRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        Job job = jobRepository.findById(jobId)
            .orElseThrow(JobNotFoundException::new);

        if(!humanResource.getCompany().getId().equals(job.getCompany().getId())){
            throw new ForbiddenException();
        }
        
        List<Application> applications = applicationRepository.findByJobId(jobId);

        return mapToList(applications);
    }

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

        if(application.getApplicationStatus() != ApplicationStatus.UNDER_REVIEW){
            throw new ApplicationReviewedException();
        }
        
        if(!humanResource.getCompany().getId().equals(job.getCompany().getId())){
            throw new ForbiddenException();
        }

        if(!application.getJob().getId().equals(job.getId())){
            throw new ForbiddenException();
        }

        application.setApplicationStatus(request.status());
        application.setHrNotes(request.hrNotes());
        
        if(application.getApplicationStatus() == ApplicationStatus.ACCEPTED){
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
            application.getHrNotes(),
            application.getApplicationStatus(),
            application.getAppliedAt()
        );
    }

    private List<ApplicationResponse> mapToList(List<Application> applications){
    return applications.stream()
            .map(this::mapToResponse)
            .toList();
    }
}
