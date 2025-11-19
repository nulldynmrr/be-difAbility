package com.ippl.difability.service;

import org.springframework.stereotype.Service;

import com.ippl.difability.entity.Application;
import com.ippl.difability.entity.Job;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.exception.ResourceConflictException;
import com.ippl.difability.exception.ResourceNotFoundException;
import com.ippl.difability.repository.ApplicationRepository;
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
    private final JobRepository jobRepository;

    @SuppressWarnings("null")
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
}
