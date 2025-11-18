package com.ippl.difability.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ippl.difability.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByIdentifier(String identifier);
}

