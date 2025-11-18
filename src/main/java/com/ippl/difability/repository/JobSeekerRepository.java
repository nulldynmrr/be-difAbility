package com.ippl.difability.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ippl.difability.entity.JobSeeker;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long>{
    Optional<JobSeeker> findByIdentifier(String identifier);   
}
