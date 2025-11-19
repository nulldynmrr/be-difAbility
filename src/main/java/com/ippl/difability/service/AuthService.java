package com.ippl.difability.service;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ippl.difability.dto.AdminLoginRequest;
import com.ippl.difability.dto.AuthResponse;
import com.ippl.difability.dto.LoginRequest;
import com.ippl.difability.dto.RegisterRequest;
import com.ippl.difability.entity.Admin;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.entity.User;
import com.ippl.difability.enums.Role;
import com.ippl.difability.exception.ForbiddenException;
import com.ippl.difability.exception.InvalidCredentialsException;
import com.ippl.difability.exception.ResourceConflictException;
import com.ippl.difability.repository.UserRepository;
import com.ippl.difability.repository.AdminRepository;
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
    private final JwtUtil jwtUtil;
    private final ActivityLogService activityLogService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @SuppressWarnings("null")
    public AuthResponse register(RegisterRequest request){
        if(userRepository.existsByIdentifier(request.getEmail())){
            throw new ResourceConflictException("Email already exists.");
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
            roleName + " registered a new account"
        );

        String token = jwtUtil.generateToken(email, roleName);
        return new AuthResponse(token, roleName);
    }

    @SuppressWarnings("null")
    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByIdentifier(request.getIdentifier())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid identifier or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid identifier or password.");
        }

        String identifier = Objects.requireNonNull(user.getIdentifier(), "Identifier cannot be null.");
        String roleName = Objects.requireNonNull(user.getRole(), "Role cannot be null.").name();

        activityLogService.log(
            identifier,
            roleName,
            "LOGIN",
            roleName + " logged in"
        );

        String token = jwtUtil.generateToken(identifier, roleName);
        return new AuthResponse(token, roleName);
    }

    @SuppressWarnings("null")
    public AuthResponse loginAdmin(AdminLoginRequest request){
        Admin admin = adminRepository.findByIdentifier(request.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

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
            roleName + " logged in with OTP"
        );

        String token = jwtUtil.generateToken(email, roleName);
        return new AuthResponse(token, roleName);
    }

     private boolean verifyOtp(String secret, String code){
        CodeVerifier verifier = new DefaultCodeVerifier(
            new DefaultCodeGenerator(HashingAlgorithm.SHA1),
            new SystemTimeProvider()
        );
        return verifier.isValidCode(secret, code);
    }
}