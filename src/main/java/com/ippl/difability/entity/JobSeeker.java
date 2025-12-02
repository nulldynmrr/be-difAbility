package com.ippl.difability.entity;

import java.util.ArrayList;
import java.util.List;

import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.EducationLevel;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "job_seekers")
@PrimaryKeyJoinColumn(name = "id")
public class JobSeeker extends User {
    @Column(name = "full_name", length = 50)
    private String fullName;

    @Column(name = "about_me", length = 500)
    private String about;

    @Column(name = "address", length = 150)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "disability_type", length = 10)
    private DisabilityType disabilityType;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "job_seeker_skills", joinColumns 
        = @JoinColumn(name = "job_seeker_id", nullable = false)
    )
    @Column(name = "skill", nullable = false, length = 50)
    private List<String> skills = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "job_seeker_certifications", joinColumns 
        = @JoinColumn(name = "job_seeker_id", nullable = false)
    )
    @Column(name = "certification_file_path", nullable = false, length = 100)
    private List<String> certificationFilePaths = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(name = "education_level", length = 20)
    private EducationLevel educationLevel;

    @Column(name = "pp_image_path", length = 100)
    private String ppImagePath;   

    @Column(name = "cv_document_path", length = 100)
    private String cvDocumentPath;   
}
