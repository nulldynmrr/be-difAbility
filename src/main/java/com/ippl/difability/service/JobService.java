package com.ippl.difability.service;

import org.springframework.stereotype.Service;

import com.ippl.difability.dto.CreateJobRequest;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.entity.Job;
import com.ippl.difability.exception.ResourceNotFoundException;
import com.ippl.difability.repository.HumanResourceRepository;
import com.ippl.difability.repository.JobRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class JobService {
    private final JobRepository jobRepository;
    private final ActivityLogService activityLogService;
    private final HumanResourceRepository humanResourceRepository;

    public Job createJob(String identifier, CreateJobRequest request) {
        HumanResource humanResource = humanResourceRepository.findByIdentifier(identifier)
            .orElseThrow(() -> new ResourceNotFoundException("HR not found!"));

        Job job = new Job();
        job.setCompany(humanResource.getCompany());
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setSalary(request.getSalary());
        job.setMinimumEducation(request.getMinimumEducation());
        job.setMinimumYearsExperience(request.getMinimumYearsExperience());
        job.setCompatibleDisabilities(request.getCompatibleDisabilities());
        job.setRegistrationDeadline(request.getRegistrationDeadline());
        job.setPublicationStatus(request.getPublicationStatus());

        activityLogService.log(
            humanResource.getUsername(),
            humanResource.getRole().name(),
            "CREATE_JOB",
            humanResource.getRole() + "created job: " + request.getTitle()
        );
        return jobRepository.save(job);
    }
}
