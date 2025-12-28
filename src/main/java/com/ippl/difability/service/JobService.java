package com.ippl.difability.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ippl.difability.dto.request.JobRequest;
import com.ippl.difability.dto.response.JobResponse;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.entity.Job;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.entity.User;
import com.ippl.difability.enums.PublicationStatus;
import com.ippl.difability.enums.Role;
import com.ippl.difability.exception.ForbiddenException;
import com.ippl.difability.exception.JobNotFoundException;
import com.ippl.difability.exception.UserNotFoundException;
import com.ippl.difability.repository.HumanResourceRepository;
import com.ippl.difability.repository.JobRepository;
import com.ippl.difability.repository.UserRepository;
import org.springframework.security.core.Authentication;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class JobService {
    private final LogService logService;
    private final UserRepository userRepository;
    private final HumanResourceRepository humanResourceRepository;
    private final JobRepository jobRepository;

    public JobResponse getJob(Long jobId){
        Job job = jobRepository.findById(jobId)
            .orElseThrow(JobNotFoundException::new);

        return mapToResponse(job);
    }

    public void deletejob(String username, Long jobId){
        User user = userRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
        
        Job job = jobRepository.findById(jobId)
            .orElseThrow(JobNotFoundException::new);
        
        Role role = user.getRole();
        Long companyId = switch (role){
        case COMPANY -> 
            ((Company) user).getId();
        case HUMAN_RESOURCE -> 
            ((HumanResource) user).getCompany().getId();
        default -> 
            throw new ForbiddenException();
        };

        if(!companyId.equals(job.getCompany().getId())){
            throw new ForbiddenException();
        }

        jobRepository.delete(job);

       logService.log(
            username,
            user.getRole().name(),
            "DELETE_JOB",
            "Menghapus lowongan kerja: " + job.getTitle()
        );

    }

public List<JobResponse> getJobs(Authentication auth) {
    List<Job> jobs;

    if(auth == null){
        jobs = jobRepository.findByPublicationStatus(PublicationStatus.OPEN);
        return mapToList(jobs);
    }

    User user = userRepository.findByUsername(auth.getName())
        .orElseThrow(UserNotFoundException::new);

    Role role = user.getRole();

    if(role == Role.ADMIN){
        jobs = jobRepository.findAllByOrderByCreatedAtDesc();
    } 
    else if(role == Role.JOB_SEEKER){
        JobSeeker jobSeeker = (JobSeeker) user;

        if(jobSeeker.getDisabilityType() == null){
            jobs = jobRepository.findByPublicationStatus(PublicationStatus.OPEN);
        } else {
            jobs = jobRepository.findByPublicationStatusAndCompatibleDisabilities(
                PublicationStatus.OPEN,
                jobSeeker.getDisabilityType()
            );
        }
    } 
    else if(role == Role.COMPANY){
        jobs = jobRepository.findByCompanyUsername(auth.getName());
    } 
    else if(role == Role.HUMAN_RESOURCE){
        jobs = jobRepository.findByHumanResourceUsername(auth.getName());
    } 
    else {
        throw new ForbiddenException();
    }

    return mapToList(jobs);
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
        job.setJobType(request.jobType());
        job.setMinimumYearsExperience(request.minimumYearsExperience());
        job.setCompatibleDisabilities(request.compatibleDisabilities());
        job.setRegistrationDeadline(request.registrationDeadline());
        job.setJobType(request.jobType());
        jobRepository.save(job);

        logService.log(
            username,
            humanResource.getRole().name(),
            "CREATE_JOB",
            "Membuat lowongan kerja: " + job.getTitle()
        );
    }

    private List<JobResponse> mapToList(List<Job> jobs){
    return jobs.stream()
            .map(this::mapToResponse)
            .toList();
    }

    private JobResponse mapToResponse(Job job){
        return new JobResponse(
            job.getId(),
            job.getCompany().getId(),
            job.getCompany().getCompanyName(),
            job.getCompany().getLogoImagePath(),
            job.getTitle(),
            job.getJobDescription(),
            job.getSalary(),
            job.getMinimumEducation(),
            job.getJobType(),
            job.getMinimumYearsExperience(),
            job.getCompatibleDisabilities(),
            job.getRegistrationDeadline(),
            job.getPublicationStatus()
        );
    }
}
