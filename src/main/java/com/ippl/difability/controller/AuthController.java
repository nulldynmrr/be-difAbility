package com.ippl.difability.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ippl.difability.dto.request.AdminLoginRequest;
import com.ippl.difability.dto.request.GeneralLoginRequest;
import com.ippl.difability.dto.request.RegistrationRequest;
import com.ippl.difability.dto.response.AuthResponse;
import com.ippl.difability.security.CookieUtil;
import com.ippl.difability.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private static final long SECONDS = 86400;

    @PostMapping("/registration")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegistrationRequest request,
            HttpServletResponse response){
        AuthResponse authResponse = authService.register(request);
        CookieUtil.setJwtCookie(response, authResponse.token(), SECONDS);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/session")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody GeneralLoginRequest request,
            HttpServletResponse response){
        AuthResponse authResponse = authService.login(request);
        CookieUtil.setJwtCookie(response, authResponse.token(), SECONDS);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/admin-session")
    public ResponseEntity<AuthResponse> loginAdmin(
            @Valid @RequestBody AdminLoginRequest request,
            HttpServletResponse response){
        AuthResponse authResponse = authService.loginAdmin(request);
        CookieUtil.setJwtCookie(response, authResponse.token(), SECONDS);
        return ResponseEntity.ok(authResponse);
    }

    @DeleteMapping("/session")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        CookieUtil.setJwtCookie(response, "", 0);
        return ResponseEntity.noContent().build();
    }
}