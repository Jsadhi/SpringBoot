package com.example.SpringSecurity.JWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtSecret}")
    private String secret;
    @Value("${spring.app.jwtExpiresInMs}")
    private int expiresInMs;

    public String getJwtFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        logger.debug("authHeader: " + authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    public String generateTokenFromUsername(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime()+expiresInMs))
                .signWith(key())
                .compact();
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey)key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }
    public Boolean validateToken(String token) {
        try
        {
            Jwts.parser().verifyWith((SecretKey)key())
                    .build().parseSignedClaims(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
