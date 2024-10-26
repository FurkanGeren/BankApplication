package com.bank.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Objects;

@Component
public class JwtUtil {

    @Value("${secret.key}")
    private String secret;

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void isTokenExpired(String token) {
        this.getAllClaimsFromToken(token).getExpiration();
    }

    public void isInvalid(String token) {
        this.isTokenExpired(token);
    }

    public List<String> extractRoles(String token) {
        Claims claims = getAllClaimsFromToken(token);

        Object roles = claims.get("role");
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .toList();
        }
        return null;
    }
}