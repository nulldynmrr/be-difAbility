package com.ippl.difability.service;

import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.ippl.difability.exception.ForbiddenException;
import com.ippl.difability.exception.InvalidCredentialsException;
import com.ippl.difability.exception.ResourceConflictException;
import com.ippl.difability.repository.UserRepository;
import com.ippl.difability.security.JwtUtil;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.time.SystemTimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ActivityLogService activityLogService;
    private final PasswordEncoder passwordEncoder;

    @SuppressWarnings("null")
    public AuthResponse register(RegisterRequest request){
        if(userRepository.existsByIdentifier(request.getEmail())){
            throw new ResourceConflictException("Email already exists");
        }

        Role role = request.getRole();
        User user = switch (role){
            case JOB_SEEKER -> new JobSeeker();
            case COMPANY -> new Company();
            default -> throw new ForbiddenException("Registration not allowed.");
        };

        user.setIdentifier(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        
        String email = Objects.requireNonNull(user.getIdentifier(), "Email cannot be null");
        String roleName = Objects.requireNonNull(role, "Role cannot be null").name();

        activityLogService.log(
            email,
            roleName,
            "REGISTER",
            user.getRole() + " registered a new account.");

        String token = jwtUtil.generateToken(email, roleName);
        return new AuthResponse(token, user.getRole().name());
    }

    @SuppressWarnings("null")
    public AuthResponse login(LoginRequest request){
        String inputIdentifier = request.getIdentifier(); 

        User user = userRepository.findByIdentifier(inputIdentifier)
            .orElseThrow(() -> new InvalidCredentialsException("Invalid identifier or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid identifier or password.");
        }

        String identifier = Objects.requireNonNull(user.getIdentifier(), "Identifier cannot be null");
        String roleName = Objects.requireNonNull(user.getRole(), "Role cannot be null").name();

        activityLogService.log(identifier, roleName, "LOGIN", roleName + " logged in.");

        String token = jwtUtil.generateToken(identifier, roleName);
        return new AuthResponse(token, roleName);
    }

    @SuppressWarnings("null")
    public AuthResponse loginAdmin(AdminLoginRequest request){
        User user = userRepository.findByIdentifier(request.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if(user.getRole() != Role.ADMIN || !(user instanceof Admin admin)){
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if(!passwordEncoder.matches(request.getPassword(), admin.getPassword())){
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        String otpSecret = "abcd1234";
        if(!verifyOtp(otpSecret, request.getOtp())){
            throw new InvalidCredentialsException("Wrong OTP code");      
        }

        String email = Objects.requireNonNull(admin.getIdentifier(), "Email cannot be null.");
        String roleName = Objects.requireNonNull(admin.getRole(), "Role cannot be null.").name();

        activityLogService.log(
            email,
            roleName,
            "LOGIN_ADMIN",
            "Admin logged in with OTP."
        );

        String token = jwtUtil.generateToken(email, roleName);
        return new AuthResponse(token, roleName);
    }

    public HrAccountResponse generateHr(Company company){
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
            "Generated humanResource account: " + username);

        return new HrAccountResponse(username, rawPassword, humanResource.getRole().name());
    }

     private boolean verifyOtp(String secret, String code){
        CodeVerifier verifier = new DefaultCodeVerifier(
            new DefaultCodeGenerator(HashingAlgorithm.SHA1),
            new SystemTimeProvider()
        );
        return verifier.isValidCode(secret, code);
    }
    private String generateUsername(String email){
        return 
            email.split("@")[0] +
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
