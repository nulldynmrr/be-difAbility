package com.ippl.difability.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ippl.difability.entity.Admin;
import com.ippl.difability.enums.Role;
import com.ippl.difability.repository.AdminRepository;
import com.ippl.difability.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL}") 
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Value("${ADMIN_TOTP_SECRET}")
    private String adminTotpSecret;

    @Bean
    CommandLineRunner initializeAdmin(UserRepository userRepository){
        return args -> {
            if(!userRepository.existsByUsername(adminEmail)){
                Admin admin = new Admin();
                admin.setUsername(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(Role.ADMIN);
                admin.setProfileCompleted(true); 
                admin.setTotpSecret(adminTotpSecret); 
                adminRepository.save(admin);
            }
        };
    }
}