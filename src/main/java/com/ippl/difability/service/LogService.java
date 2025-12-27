package com.ippl.difability.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ippl.difability.dto.response.LogResponse;
import com.ippl.difability.entity.Log;
import com.ippl.difability.enums.Role;
import com.ippl.difability.repository.LogRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LogService {
    private final LogRepository logRepository;

    public void log(String actorUsername, String actorRole, String action, String description){
        Log log = new Log();
        log.setActorUsername(actorUsername);
        log.setActorRole(actorRole);
        log.setAction(action);
        log.setDescription(description != null ? description : "No additional info");
        logRepository.save(log);
    }



    public List<LogResponse> getLogs(){
        return mapToList(
            logRepository.findAllByOrderByCreatedAtDesc()
        );
    }

    public List<LogResponse> getLogsByRole(Role role){
        return mapToList(
            logRepository.findByActorRoleOrderByCreatedAtDesc(role.name())
        );
    }

    private List<LogResponse> mapToList(List<Log> logs){
        return logs.stream()
            .map(this::mapToResponse)
            .toList();
    }

    private LogResponse mapToResponse(Log log){
        return new LogResponse(
            log.getId(),
            log.getActorUsername(),
            log.getActorRole(),
            log.getAction(),
            log.getCreatedAt()
        );
    }

    // public List<LogResponse> getLogsByUsername(String username){
    //     return mapToList(
    //         logRepository.findByActorUsernameOrderByCreatedAtDesc(username)
    //     );
    // }
 
    // public List<LogResponse> getLogsByAction(String action){
    //     return mapToList(
    //         logRepository.findByActionOrderByCreatedAtDesc(action)
    //     );
    // }
}
 