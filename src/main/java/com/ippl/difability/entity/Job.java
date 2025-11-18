package com.ippl.difability.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ippl.difability.enums.DisabilityType;
import com.ippl.difability.enums.EducationLevel;
import com.ippl.difability.enums.PublicationStatus;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;

    private String title;

    @Column(length = 2000)
    private String description;

    private Double salary;

    @Enumerated(EnumType.STRING)
    private EducationLevel minimumEducation;

    private Integer minimumYearsExperience;

    @ElementCollection(targetClass = DisabilityType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "job_compatible_disabilities", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "disability_type")
    private List<DisabilityType> compatibleDisabilities = new ArrayList<>();

    private LocalDateTime registrationDeadline;

    @Enumerated(EnumType.STRING)
    private PublicationStatus publicationStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist 
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
