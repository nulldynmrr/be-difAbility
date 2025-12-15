package com.ippl.difability.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ippl.difability.dto.request.JobRequest;
import com.ippl.difability.dto.response.JobResponse;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.entity.Job;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.entity.User;
import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.PublicationStatus;
import com.ippl.difability.enums.Role;
import com.ippl.difability.exception.ForbiddenException;
import com.ippl.difability.exception.UserNotFoundException;
import com.ippl.difability.repository.HumanResourceRepository;
import com.ippl.difability.repository.JobRepository;
import com.ippl.difability.repository.JobSeekerRepository;
import com.ippl.difability.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class JobService {
    private final LogService logService;
    private final UserRepository userRepository;
    private final HumanResourceRepository humanResourceRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final JobRepository jobRepository;

    public List<JobResponse> getJobs(String username){
        User user = userRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
        
        Role role = user.getRole();
        List<Job> jobs = switch(role){
            case ADMIN ->
                jobRepository.findAllByOrderByCreatedAtDesc();
            case JOB_SEEKER ->
                jobRepository.findByPublicationStatusAndCompatibleDisabilitiesContaining(
                    PublicationStatus.OPEN,
                    getJobSeekerDisability(username)
                );
            case COMPANY, HUMAN_RESOURCE ->
                jobRepository.findByCompanyUsername(username);
            default ->
                throw new ForbiddenException();
        };
        return mapList(jobs);
    }

    public void createJob(String username, JobRequest request){
        HumanResource humanResource = humanResourceRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
        
        Job job = new Job();
        job.setCompany(humanResource.getCompany());
        job.setTitle(request.title());
        job.setJobDescription(request.jobDescription());
        job.setSalary(request.salary());
        job.setMinimumEducation(request.minimumEducation());
        job.setMinimumYearsExperience(request.minimumYearsExperience());
        job.setCompatibleDisabilities(request.compatibleDisabilities());
        job.setRegistrationDeadline(request.registrationDeadline());
        jobRepository.save(job);

        logService.log(
            username,
            humanResource.getRole().name(),
            "CREATE_JOB",
            "Created new job " + job.getTitle()
        );
    }


    private List<JobResponse> mapList(List<Job> jobs){
    return jobs.stream()
            .map(this::mapToResponse)
            .toList();
    }

    private JobResponse mapToResponse(Job job){
        return new JobResponse(
            job.getId(),
            job.getCompany().getCompanyName(),
            job.getCompany().getLogoImagePath(),
            job.getTitle(),
            job.getJobDescription(),
            job.getSalary(),
            job.getMinimumEducation(),
            job.getMinimumYearsExperience(),
            job.getCompatibleDisabilities(),
            job.getRegistrationDeadline(),
            job.getPublicationStatus()
        );
    }

    private DisabilityType getJobSeekerDisability(String username){
    JobSeeker jobSeeker = jobSeekerRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
            
    return jobSeeker.getDisabilityType();
    }
}
