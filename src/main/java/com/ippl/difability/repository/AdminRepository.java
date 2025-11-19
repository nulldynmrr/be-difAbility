package com.ippl.difability.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ippl.difability.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{
    Optional<Admin> findByIdentifier(String identifier);   
}