package com.ippl.difability.entity;

import com.ippl.difability.enums.IndustryType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "companies")
@PrimaryKeyJoinColumn(name = "id")
public class Company extends User {
    @Column(name = "company_name", length = 50)
    private String companyName;

    @Column(name = "company_description", length = 500)
    private String companyDescription;

    @Column(name = "address", length = 150)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry_type", length = 15)
    private IndustryType industryType;

    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;
    
    @Column(name = "youtube_url", length = 255)
    private String youtubeUrl;

    @Column(name = "instagram_url", length = 255)
    private String instagramUrl;

    @Column(name = "twitter_url", length = 255)
    private String twitterUrl;
    
    @Column(name = "logo_image_path", length = 100)
    private String logoImagePath;

    // @OneToMany(
    //     mappedBy = "company", cascade = CascadeType.ALL,
    //     orphanRemoval = true, fetch = FetchType.LAZY
    // )
    // private List<Job> jobs = new ArrayList<>();

    // @OneToMany(
    //     mappedBy = "company", cascade = CascadeType.ALL,
    //     orphanRemoval = true, fetch = FetchType.LAZY
    // )
    // @JsonManagedReference
    // private List<HumanResource> humanResources = new ArrayList<>();
}
