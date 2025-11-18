package com.ippl.difability.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ippl.difability.repository.UserRepository;
import com.ippl.difability.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        
        User user = userRepository.findByIdentifier(identifier)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + identifier));
        
        return org.springframework.security.core.userdetails.User
            .withUsername(identifier)
            .password(user.getPassword())
            .roles(user.getRole().name())
            .build();
    }
}