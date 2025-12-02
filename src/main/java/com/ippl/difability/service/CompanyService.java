package com.ippl.difability.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ippl.difability.dto.response.HrCredentialResponse;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.enums.Role;
import com.ippl.difability.exception.UserNotFoundException;
import com.ippl.difability.repository.CompanyRepository;
import com.ippl.difability.repository.HumanResourceRepository;
import com.ippl.difability.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    private final LogService logService;
    private final CompanyRepository companyRepository;
    private final HumanResourceRepository humanResourceRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public HrCredentialResponse generateHrAccount(String username) {
        Company company = companyRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        String newUsername = generateUsername(username);
        String newPassword = generatePassword();

        HumanResource humanResource = new HumanResource();
        humanResource.setUsername(newUsername);
        humanResource.setPassword(passwordEncoder.encode(newPassword));
        humanResource.setRole(Role.HUMAN_RESOURCE);
        humanResource.setCompany(company);
        humanResourceRepository.save(humanResource);

        logService.log(
            username,
            company.getRole().name(), 
            "CREATE_HR", 
            "Generated new HR account: " + newUsername
        );
        return new HrCredentialResponse(newUsername, newPassword);
    }
    
    private String generateUsername(String username) {
        boolean usernameExists;
        String newUsername;
        do{
            String baseName = username.split("@")[0];
            String uniqueId = RandomStringUtils.secure().next(6, true, true);
            newUsername = baseName + "_hr_" + uniqueId;
            usernameExists = !userRepository.existsByUsername(username);
        }while(usernameExists);
        return newUsername;
    }
    
    private String generatePassword() {
        String upperCaseLetters = RandomStringUtils.secure().next(4, true, false).toUpperCase();
        String alphanumerics = RandomStringUtils.secure().next(8, true, true);
        String digits = RandomStringUtils.secure().next(4, false, true);
        return upperCaseLetters + alphanumerics + digits;
    }
}