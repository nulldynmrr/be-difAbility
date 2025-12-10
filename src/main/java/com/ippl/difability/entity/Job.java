package com.ippl.difability.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    private Company company;
    
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "job_description", length = 1000)
    private String jobDescription;

    @Column(name = "salary", precision = 12, scale = 2) // max Rp9.999.999.999,99
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(name = "minimum_education", length = 20)
    private EducationLevel minimumEducation;

    @Column(name = "min_years_experience")
    private Integer minimumYearsExperience;

    @ElementCollection(targetClass = DisabilityType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "job_compatible_disabilities",
                     joinColumns = @JoinColumn(name = "job_id", nullable = false))
    @Column(name = "disability_type", nullable = false, length = 10)
    private List<DisabilityType> compatibleDisabilities = new ArrayList<>();

    @Column(name = "registration_deadline")
    private LocalDateTime registrationDeadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = false, length = 10)
    private PublicationStatus publicationStatus = PublicationStatus.OPEN;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
