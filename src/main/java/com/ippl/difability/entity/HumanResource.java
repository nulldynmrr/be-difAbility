package com.ippl.difability.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "human_resources")
@PrimaryKeyJoinColumn(name = "id")
public class HumanResource extends User {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    private Company company;

    @Column(name = "full_name", length = 50)
    private String fullName;

    @Column(name = "contact", length = 15)
    private String contact;

    @Column(name = "pp_image_path", length = 100)
    private String ppImagePath;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
