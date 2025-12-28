// package com.ippl.difability.security;

// import java.nio.charset.StandardCharsets;
// import java.security.Key;
// import java.util.Date;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.lang.NonNull;
// import org.springframework.stereotype.Component;

// import com.ippl.difability.enums.Role;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.JwtException;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;

// @Component
// public class JwtUtil {
//     private final Key key;
//     private final long expirationMiliSecond;

//     public JwtUtil(
//         @Value("${jwt.secret}") @NonNull String secretKey,
//         @Value("${jwt.expiration-ms}") long expirationMiliSecond) {
//         this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
//         this.expirationMiliSecond = expirationMiliSecond;
//     }

//     public String generateToken(@NonNull String username, @NonNull String role) {
//         long now = System.currentTimeMillis();
//         return Jwts.builder()
//             .setSubject(username)
//             .claim("role", role)
//             .setIssuedAt(new Date(now))
//             .setExpiration(new Date(now + this.expirationMiliSecond))
//             .signWith(this.key, SignatureAlgorithm.HS256)
//             .compact();
//     }

//     public String generateToken(@NonNull String username, @NonNull Role role, @NonNull Long id, Long companyId) {
//         long now = System.currentTimeMillis();
//         Claims claims = Jwts.claims().setSubject(username);
//         claims.put("role", role.name()); 
//         claims.put("id", id);
//         if (companyId != null) claims.put("companyId", companyId);

//         return Jwts.builder()
//             .setClaims(claims)
//             .setIssuedAt(new Date(now))
//             .setExpiration(new Date(now + this.expirationMiliSecond))
//             .signWith(this.key, SignatureAlgorithm.HS256)
//             .compact();
//     }

//     public boolean validateToken(@NonNull String token) {
//         try {
//             Jwts.parserBuilder()
//                 .setSigningKey(key)
//                 .build()
//                 .parseClaimsJws(token);
//             return true;
//         } catch (JwtException e) {
//             return false;
//         }
//     }

//     private Claims getClaims(@NonNull String token) {
//         return Jwts.parserBuilder()
//             .setSigningKey(key)
//             .build()
//             .parseClaimsJws(token)
//             .getBody();
//     }

//     public String extractUsername(@NonNull String token) {
//         return getClaims(token).getSubject();
//     }

//     public String extractRole(@NonNull String token) {
//         return (String) getClaims(token).get("role");
//     }

//     public Long extractId(@NonNull String token) {
//         Object value = getClaims(token).get("id");
//         return value != null ? ((Number) value).longValue() : null;
//     }

//     public Long extractCompanyId(@NonNull String token) {
//         Object value = getClaims(token).get("companyId");
//         return value != null ? ((Number) value).longValue() : null;
//     }
// }

package com.ippl.difability.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.ippl.difability.enums.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final Key key;
    private final long expirationMiliSecond;

    public JwtUtil(
        @Value("${jwt.secret}") @NonNull String secretKey,
        @Value("${jwt.expiration-ms}") long expirationMiliSecond) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMiliSecond = expirationMiliSecond;
    }

    // Generate token simple (username + role)
    public String generateToken(@NonNull String username, @NonNull String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
            .setSubject(username)
            .claim("role", role)
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + this.expirationMiliSecond))
            .signWith(this.key, SignatureAlgorithm.HS256)
            .compact();
    }

    // Generate token dengan id dan companyId (HR)
    public String generateToken(@NonNull String username, @NonNull Role role, @NonNull Long id, Long companyId) {
        long now = System.currentTimeMillis();
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role.name());
        claims.put("id", id);
        if (companyId != null) claims.put("companyId", companyId);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + this.expirationMiliSecond))
            .signWith(this.key, SignatureAlgorithm.HS256)
            .compact();
    }

    // Validasi token
    public boolean validateToken(@NonNull String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Ambil claims dari token
    private Claims getClaims(@NonNull String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String extractUsername(@NonNull String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(@NonNull String token) {
        return (String) getClaims(token).get("role");
    }

    public Long extractId(@NonNull String token) {
        Object value = getClaims(token).get("id");
        return value != null ? ((Number) value).longValue() : null;
    }

    public Long extractCompanyId(@NonNull String token) {
        Object value = getClaims(token).get("companyId");
        return value != null ? ((Number) value).longValue() : null;
    }
}
