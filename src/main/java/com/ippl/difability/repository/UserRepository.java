package com.ippl.difability.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ippl.difability.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u JOIN u.humanResources h WHERE h.username = :username")
    Optional<User> findByHumanResourcesUsername(@Param("username") String username);
    boolean existsByEmail(String email);
}
