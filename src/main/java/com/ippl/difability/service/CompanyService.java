package com.ippl.difability.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ippl.difability.dto.HrAccountResponse;
import com.ippl.difability.entity.Company;
import com.ippl.difability.repository.CompanyRepository;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.enums.Role;
import com.ippl.difability.exception.ResourceNotFoundException;
import com.ippl.difability.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {
    private final ActivityLogService activityLogService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public HrAccountResponse generateHr(String identifier){
        Company company = companyRepository.findByIdentifier(identifier)
            .orElseThrow(() -> new ResourceNotFoundException("Company not found."));

        String username = generateUsername(company.getIdentifier());
        String rawPassword = generatePassword();

        HumanResource humanResource = new HumanResource();
        humanResource.setIdentifier(username);
        humanResource.setUsername(username);
        humanResource.setPassword(passwordEncoder.encode(rawPassword));
        humanResource.setRole(Role.HUMAN_RESOURCE);
        humanResource.setCompany(company);
        userRepository.save(humanResource);

        activityLogService.log(
            company.getIdentifier(),
            company.getRole().name(),
            "GENERATE_HR",
            company.getRole().name() + " generated HR account: " + username
        );

        return new HrAccountResponse(username, rawPassword, humanResource.getRole().name());
    }

    private String generateUsername(String identifier){
        return 
            identifier.split("@")[0] +
            "_hr_" +
            RandomStringUtils.secure().next(4, false, true);
    }

    private String generatePassword(){
        String uppercaseLetters = RandomStringUtils.secure().next(2, true, false).toUpperCase();
        String alphanumerics = RandomStringUtils.secure().next(8, true, true);
        String digits = RandomStringUtils.secure().next(2, false, true);
        return uppercaseLetters + alphanumerics + digits;
    }
}