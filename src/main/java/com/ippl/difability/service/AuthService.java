package com.ippl.difability.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ippl.difability.dto.AdminLoginRequest;
import com.ippl.difability.dto.AuthResponse;
import com.ippl.difability.dto.HrAccountResponse;
import com.ippl.difability.dto.LoginRequest;
import com.ippl.difability.dto.RegisterRequest;
import com.ippl.difability.entity.Admin;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.HumanResource;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.entity.User;
import com.ippl.difability.enums.Role;
import com.ippl.difability.repository.UserRepository;
import com.ippl.difability.security.JwtUtil;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.time.SystemTimeProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthResponse register(RegisterRequest req){
        Role role = Role.valueOf(req.getRole().toUpperCase());
        if(role != Role.COMPANY && role != Role.JOB_SEEKER)
            throw new RuntimeException("Only COMPANY or JOB_SEEKER can register");

        if(userRepository.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already exists");

        User user = (role == Role.COMPANY) ? new Company() : new JobSeeker();
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name());
    }

    public AuthResponse login(LoginRequest req){
        String id = req.getIdentifier();

        User user = userRepository.findByEmail(id)
                .or(() -> userRepository.findAll().stream()
                        .filter(u -> u instanceof HumanResource hr && hr.getUsername().equals(id))
                        .findFirst())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!encoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid credentials");

        String identifier = (user instanceof HumanResource hr)
                ? hr.getUsername()
                : user.getEmail();

        String token = jwtUtil.generateToken(identifier, user.getRole().name());
        return new AuthResponse(token, user.getRole().name());
    }

    public AuthResponse loginAdmin(AdminLoginRequest req){
        Admin admin = (Admin) userRepository.findByEmail(req.getEmail())
                .filter(u -> u instanceof Admin)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if(!encoder.matches(req.getPassword(), admin.getPassword()))
            throw new RuntimeException("Wrong password");

        if(!verifyOtp(admin.getTotpSecret(), req.getOtp()))
            throw new RuntimeException("Wrong OTP code");

        String token = jwtUtil.generateToken(admin.getEmail(), admin.getRole().name());
        return new AuthResponse(token, admin.getRole().name());
    }

    private boolean verifyOtp(String secret, String code){
        CodeVerifier verifier = new DefaultCodeVerifier(
                new DefaultCodeGenerator(HashingAlgorithm.SHA1),
                new SystemTimeProvider()
        );
        return verifier.isValidCode(secret, code);
    }

    private String generatePassword(){
        String upper = RandomStringUtils.secure().next(2, true, false).toUpperCase();
        String digit = RandomStringUtils.secure().next(2, false, true);
        String rest = RandomStringUtils.secure().next(8, true, true);
        return upper + digit + rest;
    }

    public HrAccountResponse generateHr(String companyEmail){
        Company company = (Company) userRepository.findByEmail(companyEmail)
                .filter(u -> u instanceof Company)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        String username = company.getEmail().split("@")[0] + "_hr_" + RandomStringUtils.secure().next(3, false, true);
        String rawPassword = generatePassword();

        HumanResource hr = new HumanResource();
        hr.setUsername(username);
        hr.setPassword(encoder.encode(rawPassword));
        hr.setRole(Role.HUMAN_RESOURCE);
        hr.setCompany(company);
        userRepository.save(hr);

        return new HrAccountResponse(username, rawPassword, hr.getRole().name());
    }
}
