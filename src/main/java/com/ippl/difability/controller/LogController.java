package com.ippl.difability.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.response.LogResponse;
import com.ippl.difability.enums.Role;
import com.ippl.difability.service.LogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<LogResponse>> getAllLogs(){
        List<LogResponse> logs = logService.findAllLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/users/{username}")
    @PreAuthorize("hasAuthority('ADMIN')") 
    public ResponseEntity<List<LogResponse>> getLogsByUsername(@PathVariable String username){
        List<LogResponse> logs = logService.findLogsByUsername(username);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/roles/{role}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<LogResponse>> getLogsByRole(@PathVariable Role role){
        List<LogResponse> logs = logService.findLogsByRole(role);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/actions/{action}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<LogResponse>> getLogsByAction(@PathVariable String action){
        List<LogResponse> logs = logService.findLogsByAction(action);
        return ResponseEntity.ok(logs);
    }
}

