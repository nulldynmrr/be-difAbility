package com.ippl.difability.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ippl.difability.entity.Job;
import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.PublicationStatus;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>{
    List<Job> findAllByOrderByCreatedAtDesc();
    List<Job> findByPublicationStatusAndCompatibleDisabilitiesContaining(
        PublicationStatus status, 
        DisabilityType disabilityType
    );
    List<Job> findByCompanyUsername(String username);

}
