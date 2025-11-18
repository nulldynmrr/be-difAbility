package com.ippl.difability.service;

import org.springframework.stereotype.Service;

import com.ippl.difability.entity.ActivityLog;
import com.ippl.difability.repository.ActivityLogRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;

    public void log(String actorIdentifier, String actorRole, String action, String description) {
        ActivityLog log = new ActivityLog();
        log.setActorIdentifier(actorIdentifier);
        log.setActorRole(actorRole);
        log.setAction(action);
        log.setDescription(description);
        activityLogRepository.save(log);
    }
}
 