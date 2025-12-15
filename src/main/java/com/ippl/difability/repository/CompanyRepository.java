package com.ippl.difability.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ippl.difability.entity.Company;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{
    Optional<Company> findByUsername(String username);
    Optional<Company> findByCompanyNameContainingIgnoreCase(String companyName);
}
