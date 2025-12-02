package com.ippl.difability.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ippl.difability.entity.HumanResource;

@Repository
public interface HumanResourceRepository extends JpaRepository<HumanResource, Long>{
    Optional<HumanResource> findByUsername(String username);
}
