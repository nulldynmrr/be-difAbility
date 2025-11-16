package com.ippl.difability.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.entity.ActivityLog;
import com.ippl.difability.repository.ActivityLogRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ActivityLogController {
    private final ActivityLogRepository activityLogRepository;

    @GetMapping("/logs")
    public List<ActivityLog> getAllLogs() {
        return activityLogRepository.findAll();
    }
}

