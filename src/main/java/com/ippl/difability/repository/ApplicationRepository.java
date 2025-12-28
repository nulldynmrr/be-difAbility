package com.ippl.difability.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ippl.difability.entity.Application;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.entity.Job;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>{
    List<Application> findByJobSeekerId(Long jobSeekerId);
    List<Application> findByJobId(Long jobId);
    Optional<Application> findByIdAndJobCompanyId(Long applicationId, Long id);
    boolean existsByJobSeekerAndJob(JobSeeker jobSeeker, Job job);
    boolean existsByJobSeekerIdAndJobCompanyId(Long jobSeekerId, Long companyId);
}
