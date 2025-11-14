package com.ippl.difability.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.ProfileUpdateRequest;
import com.ippl.difability.entity.User;
import com.ippl.difability.security.JwtUtil;
import com.ippl.difability.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PutMapping("/update-profile")
    public User updateProfile(@RequestHeader("Authorization") String authHeader,
                              @Valid @RequestBody ProfileUpdateRequest req) {
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new RuntimeException("Missing token");

        String token = authHeader.substring(7);
        String identifier = jwtUtil.extractIdentifier(token);

        return userService.updateProfile(identifier, req);
    }
}
