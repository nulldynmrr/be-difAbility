package com.ippl.difability.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ippl.difability.entity.Admin;
import com.ippl.difability.enums.Role;
import com.ippl.difability.repository.UserRepository;

@Configuration
public class AdminInitializer {
    @SuppressWarnings("unused")
    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@ippl.com").isEmpty()) {
                Admin admin = new Admin();
                admin.setEmail("admin@ippl.com");
                admin.setPassword(new BCryptPasswordEncoder().encode("Admin1234!"));
                admin.setRole(Role.ADMIN);
                admin.setTotpSecret("JBSWY3DPEHPK3PXP");
                userRepository.save(admin);
                System.out.println("email   :admin@ippl.com");
                System.out.println("password:Admin1234!");
                System.out.println("TOTP    :JBSWY3DPEHPK3PXP");
            }
        };
    }
}
