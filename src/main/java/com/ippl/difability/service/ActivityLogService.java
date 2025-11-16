package com.ippl.difability.service;

import org.springframework.stereotype.Service;

import com.ippl.difability.entity.ActivityLog;
import com.ippl.difability.repository.ActivityLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityLogService {
    private final ActivityLogRepository logRepository;

    public void log(String actorEmail, String actorRole, String action, String description) {
        ActivityLog log = new ActivityLog();
        log.setActorEmail(actorEmail);
        log.setActorRole(actorRole);
        log.setAction(action);
        log.setDescription(description);
        logRepository.save(log);
    }
}
 