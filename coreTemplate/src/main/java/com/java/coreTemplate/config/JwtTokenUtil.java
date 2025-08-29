package com.java.coreTemplate.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtTokenUtil {
     private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //private static final Key SECRET_KEY = Keys.secretKeyFor(Jwts.SIG.HS256);
    private static final int EXPIRATION_HOURS = 24;

    public static String generateToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(DateUtils.addHours(new Date(), EXPIRATION_HOURS))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) SECRET_KEY) // Changed from setSigningKey()
                .build()
                .parseSignedClaims(token) // Changed from parseClaimsJws()
                .getPayload(); // Changed from getBody()
    }
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}