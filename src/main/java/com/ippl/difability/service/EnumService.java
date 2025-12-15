package com.ippl.difability.service;

import org.springframework.stereotype.Service;

import java.util.List;

import com.ippl.difability.enums.ApplicationStatus;
import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.EducationLevel;
import com.ippl.difability.enums.IndustryType;
import com.ippl.difability.enums.PublicationStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EnumService {
    public List<ApplicationStatus> getApplicationStatus(){
        return List.of(ApplicationStatus.values());
    }

    public List<DisabilityType> getDisabilityTypes(){
        return List.of(DisabilityType.values());
    }

    public List<EducationLevel> getEducationLevels(){
        return List.of(EducationLevel.values());
    }

    public List<IndustryType> getIndustryTypes(){
        return List.of(IndustryType.values());
    }
    
    public List<PublicationStatus> getPublicationStatuses(){
        return List.of(PublicationStatus.values());
    }
}
