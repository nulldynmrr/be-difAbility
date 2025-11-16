package com.ippl.difability.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ippl.difability.dto.ProfileUpdateRequest;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.entity.User;
import com.ippl.difability.enums.Role;
import com.ippl.difability.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ActivityLogService logService;

    @Transactional
    public User updateProfile(String identifier, ProfileUpdateRequest req){
        User user = userRepository.findByEmail(identifier)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(req == null)
            throw new RuntimeException("Invalid request");

        if(user.getRole() == Role.JOB_SEEKER){
            JobSeeker seeker = (JobSeeker) user;
            seeker.setName(req.getName());
            seeker.setAbout(req.getAbout());
            seeker.setAddress(req.getAddress());
            seeker.setDisabilityType(req.getDisabilityType());
            seeker.setSkills(req.getSkills());
            seeker.setEducationLevel(req.getEducationLevel());
            seeker.setPpImgPath(req.getPpImgPath());
            seeker.setCvFilePath(req.getCvFilePath());
            seeker.setCertifFilePaths(req.getCertifFilePaths());
            seeker.setProfileCompleted(true);

            logService.log(user.getEmail(), user.getRole().name(), "UPDATE_PROFILE",
            user.getRole() + " updated their profile.");

            return userRepository.save(seeker);

        } else if(user.getRole() == Role.COMPANY){
            Company company = (Company) user;
            company.setName(req.getName());
            company.setDescription(req.getDescription());
            company.setAddress(req.getAddress());
            company.setIndustryType(req.getIndustryType());
            company.setWebsiteUrl(req.getWebsiteUrl());
            company.setLogoImgPath(req.getLogoImgPath());
            company.setProfileCompleted(true);

            logService.log(user.getEmail(), user.getRole().name(), "UPDATE_PROFILE",
            user.getRole() + " updated their profile.");

            return userRepository.save(company);
        }
        throw new RuntimeException("Cannot update profile for this role");
    }
}
