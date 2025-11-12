package com.ippl.difability.entity;

import java.time.LocalDateTime;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PreUpdate;
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
    private String name;
    @Column(length = 1000)
    private String about;

    private String address;

    @Enumerated(EnumType.STRING)
    private DisabilityType disabilityType;

    @ElementCollection
    @CollectionTable(name = "job_seeker_skills", joinColumns = @JoinColumn(name = "job_seeker_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;

    private String ppImgPath;   
    private String cvFilePath;   

    @ElementCollection
    @CollectionTable(name = "job_seeker_certifications", joinColumns = @JoinColumn(name = "job_seeker_id"))
    @Column(name = "certification_path")
    private List<String> certifFilePaths = new ArrayList<>();

    private LocalDateTime updatedAt;

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
