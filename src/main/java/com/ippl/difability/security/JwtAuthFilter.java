package com.ippl.difability.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getServletPath();
        return path.startsWith("/api/auth/");
    }
    
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException{
        String token = getTokenFromRequest(request);

        if (token != null && jwtUtil.validateToken(token)) {
            authenticateUser(token, request);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "token");
        if (cookie != null){
            return cookie.getValue();
        }

        // // Auth Bearer (cara lama)
        // String authHeader = request.getHeader("Authorization");
        // if (authHeader != null && authHeader.startsWith("Bearer ")) {
        //     return authHeader.substring(7);
        // }

        return null;
    }

    private void authenticateUser(String token, HttpServletRequest request) {
        String username = jwtUtil.extractUsername(token);

        if(username == null || SecurityContextHolder.getContext().getAuthentication() != null){
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}