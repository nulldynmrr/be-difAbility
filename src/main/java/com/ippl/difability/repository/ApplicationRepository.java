package com.ippl.difability.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ippl.difability.entity.Application;
import com.ippl.difability.entity.Job;
import com.ippl.difability.entity.JobSeeker;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByJobSeekerAndJob(JobSeeker seeker, Job job);
}
