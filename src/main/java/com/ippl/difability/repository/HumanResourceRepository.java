package com.ippl.difability.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ippl.difability.entity.HumanResource;

public interface HumanResourceRepository extends JpaRepository<HumanResource, Long> {
    Optional<HumanResource> findByIdentifier(String identifier);
}
