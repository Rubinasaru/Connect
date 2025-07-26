package com.example.demo.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.Models.User;
import com.example.demo.Models.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;


@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private Key key() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //    public String generateToken(String subject) {
//        return Jwts.builder()
//            .setSubject(subject)
//            .setIssuedAt(new Date())
//            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
//            .signWith(key())
//            .compact();
//    }
    public String generateToken(User user) {
        UserProfile profile = user.getProfile();  // Fetch the linked profile

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());

        if (profile != null) {
            claims.put("email", profile.getEmail());
            claims.put("firstName", profile.getFirstName());
            claims.put("lastName", profile.getLastName());
            claims.put("fullName", profile.getFirstName() + " " + profile.getLastName());
            claims.put("role", profile.getRole());
            claims.put("department", profile.getDepartment());
            claims.put("interest", profile.getInterest());
            claims.put("profileImgUrl", profile.getProfileImgUrl());
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername()) // or user.getUsername()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 day
                .signWith(key())
                .compact();
    }


    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", bearerToken);

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            // Remove the "Bearer " prefix and return the token
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateTokenFromUsername(UserDetails userDetails){
        String username = userDetails.getUsername();
        return Jwts.builder()
                .setSubject(username) // Set the username as the token's subject
                .setIssuedAt(new Date()) // Set the current time as the token's subject
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs)) // Set the expiration time
                .signWith(key()) // Sign the token using the secret key
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateJwtToken(String authToken) {
        try {
            logger.debug("Validating JWT token");
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true; // Token is valid
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false; // Token is invalid
    }

}