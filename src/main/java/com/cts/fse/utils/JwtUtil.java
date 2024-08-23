package com.cts.fse.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    private final String secretkey = "sampletestkjhagkjgnalknlkanevlmvfghsvfjsdhv";

    /*
     * @param token
     * @return
     */
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);

    }

    /*
     * @param <T>
     * @param token
     * @param claimsResolver
     * @return
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    /*
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser().setSigningKey(secretkey).build().parseSignedClaims(token).getPayload();

    }

    /*
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /*
     * @param claims
     * @param subject
     * @return
     */
    private String createToken(Map<String, Object> claims, String subject) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretkey));
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))  // token for 15 min
                .signWith(key).compact();

    }

    /*
     * @param token
     * @return
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretkey).build().parseSignedClaims(token).getPayload();
            return true;
        }
        catch(Exception e) {
            return false;
        }

    }

}
