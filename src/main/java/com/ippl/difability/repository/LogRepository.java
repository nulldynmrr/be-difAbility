package com.ippl.difability.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ippl.difability.entity.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findAllByOrderByCreatedAtDesc();
    List<Log> findByActorUsernameOrderByCreatedAtDesc(String actorUsername);
    List<Log> findByActorRoleOrderByCreatedAtDesc(String actorRole);
    List<Log> findByActionOrderByCreatedAtDesc(String action);
}
