package com.ippl.difability.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ippl.difability.entity.Job;
import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.PublicationStatus;

// @Repository
// public interface JobRepository extends JpaRepository<Job, Long>{
//     List<Job> findAllByOrderByCreatedAtDesc();
//     // List<Job> findByPublicationStatusAndCompatibleDisabilitiesContaining(
//     //     PublicationStatus status, 
//     //     DisabilityType disabilityType
//     // );
//     List<Job> findByPublicationStatus(PublicationStatus status);
//     List<Job> findByCompanyUsername(String username);

//     @Query("SELECT j FROM Job j JOIN j.compatibleDisabilities d " +
//        "WHERE j.publicationStatus = :status AND d = :disabilityType")
// List<Job> findByPublicationStatusAndCompatibleDisabilities(
//     @Param("status") PublicationStatus status,
//     @Param("disabilityType") DisabilityType disabilityType
// );


// }

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByOrderByCreatedAtDesc();
    List<Job> findByPublicationStatus(PublicationStatus status);
    List<Job> findByCompanyUsername(String username);
    @Query("SELECT j FROM Job j WHERE j.company.id = " +
           "(SELECT hr.company.id FROM HumanResource hr WHERE hr.username = :username)")
    List<Job> findByHumanResourceUsername(@Param("username") String username);

    @Query("SELECT j FROM Job j JOIN j.compatibleDisabilities d " +
           "WHERE j.publicationStatus = :status AND d = :disabilityType")
    List<Job> findByPublicationStatusAndCompatibleDisabilities(
        @Param("status") PublicationStatus status,
        @Param("disabilityType") DisabilityType disabilityType
    );
}

