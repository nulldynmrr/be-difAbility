package com.ippl.difability.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ippl.difability.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByIdentifier(String identifier);
    Optional<User> findByIdentifier(String identifier);
}
