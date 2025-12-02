package com.ippl.difability.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.enums.ApplicationStatus;
import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.EducationLevel;
import com.ippl.difability.enums.IndustryType;
import com.ippl.difability.enums.PublicationStatus;
import com.ippl.difability.service.EnumService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enums")
@RequiredArgsConstructor
public class EnumController {
    private final EnumService enumService;

    @GetMapping("/application-statuses")
    public ResponseEntity<List<ApplicationStatus>> getApplicationStatus(){
        List<ApplicationStatus> applicationStatuses = enumService.getApplicationStatus();
        return ResponseEntity.ok(applicationStatuses);
    }

    @GetMapping("/disability-types")
    public ResponseEntity<List<DisabilityType>> getDisabilityType(){
        List<DisabilityType> disabilityTypes = enumService.getDisabilityTypes();
        return ResponseEntity.ok(disabilityTypes);
    }

    @GetMapping("/education-levels")
    public ResponseEntity<List<EducationLevel>> getEducationLevels(){
        List<EducationLevel> educationLevels = enumService.getEducationLevels();
        return ResponseEntity.ok(educationLevels);
    }

    @GetMapping("/industry-types")
    public ResponseEntity<List<IndustryType>> getIndustryTypes(){
        List<IndustryType> industryTypes = enumService.getIndustryTypes();
        return ResponseEntity.ok(industryTypes);
    }

    @GetMapping("/publication-statuses")
    public ResponseEntity<List<PublicationStatus>> getPublicationStatuses(){
        List<PublicationStatus> publicationStatuses = enumService.getPublicationStatuses();
        return ResponseEntity.ok(publicationStatuses);
    }
}
