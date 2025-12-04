package com.ippl.difability.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ippl.difability.dto.request.AdminLoginRequest;
import com.ippl.difability.dto.request.GeneralLoginRequest;
import com.ippl.difability.dto.request.RegistrationRequest;
import com.ippl.difability.dto.response.AuthResponse;
import com.ippl.difability.entity.Admin;
import com.ippl.difability.entity.Company;
import com.ippl.difability.entity.JobSeeker;
import com.ippl.difability.entity.User;
import com.ippl.difability.enums.Role;
import com.ippl.difability.exception.EmailAlreadyExistsException;
import com.ippl.difability.exception.ForbiddenException;
import com.ippl.difability.exception.InvalidCredentialsException;
import com.ippl.difability.repository.AdminRepository;
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
@Transactional
public class AuthService {
    private final JwtUtil jwtUtil;
    private final LogService logService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AuthResponse register(RegistrationRequest request){
        if(userRepository.existsByUsername(request.email())){
            throw new EmailAlreadyExistsException();
        }

        Role role = request.role();
        User user = switch (role){
            case JOB_SEEKER -> new JobSeeker();
            case COMPANY -> new Company();
            default -> throw new ForbiddenException();
        };

        user.setUsername(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);
        userRepository.save(user);
        
        logService.log(
            user.getUsername(),
            user.getRole().name(),
            "REGISTER"
        );

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(
            token,
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.isProfileCompleted()
        );
    }

    public AuthResponse login(GeneralLoginRequest request){
        User user = userRepository.findByUsername(request.username())
            .orElseThrow(InvalidCredentialsException::new);

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new InvalidCredentialsException();
        }

        logService.log(
            user.getUsername(),
            user.getRole().name(),
            "LOGIN"
        );

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(
            token,
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.isProfileCompleted()
        );
    }

    public AuthResponse loginAdmin(AdminLoginRequest request){
        Admin admin = adminRepository.findByUsername(request.username())
            .orElseThrow(InvalidCredentialsException::new);

        if(!passwordEncoder.matches(request.password(), admin.getPassword())){
            throw new InvalidCredentialsException();
        }
    
        if(!verifyOtp(admin.getTotpSecret(), request.otp())){
            throw new InvalidCredentialsException();    
        }

        logService.log(
            admin.getUsername(),
            admin.getRole().name(),
            "LOGIN_ADMIN"
        );

        String token = jwtUtil.generateToken(admin.getUsername(), admin.getRole().name());
        
        return new AuthResponse(
            token,
            admin.getId(),
            admin.getUsername(),
            admin.getRole(),
            admin.isProfileCompleted()
        );
    }

    private boolean verifyOtp(String secret, String code){
        CodeVerifier verifier = new DefaultCodeVerifier(
            new DefaultCodeGenerator(HashingAlgorithm.SHA1),
            new SystemTimeProvider()
        );
        return verifier.isValidCode(secret, code);
    }
}