package com.ippl.difability.service;

import org.springframework.stereotype.Service;

import com.ippl.difability.entity.Application;
import com.ippl.difability.entity.Job;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.exception.ResourceNotFoundException;
import com.ippl.difability.exception.ResourceConflictException;
import com.ippl.difability.repository.ApplicationRepository;
import com.ippl.difability.repository.JobRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ActivityLogService activityLogService;

    public Application createApplication(JobSeeker jobSeeker, Long jobId) {
        if(jobId == null){
            throw new IllegalArgumentException("Job ID must not be null."); 
        }

        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job with id " + jobId + " not found"));

        if(applicationRepository.existsByJobSeekerAndJob(jobSeeker, job)){
            throw new ResourceConflictException(jobSeeker.getIdentifier() + "has already applied to" + job.getTitle());
        }
        
        Application application = new Application();
        application.setJobSeeker(jobSeeker);
        application.setJob(job);

        activityLogService.log(
            jobSeeker.getIdentifier(),
            jobSeeker.getRole().name(),
            "CREATE_APPLICATION",
            jobSeeker.getRole() + " created an application"
        );

        return applicationRepository.save(application);
    }
}
