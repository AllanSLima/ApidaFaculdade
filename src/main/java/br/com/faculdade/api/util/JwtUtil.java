package br.com.faculdade.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET_STRING = "d2VzdGVybmNhbWVmdXJ0aGVyc2V0dGxld3Jvbmd1cHRlYW1yZXBsaWVkZHJpdmVmb3I=";

    private final Key SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_STRING));

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    public String generateToken(String ra, String nome) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("nome", nome);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(ra)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractRa(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractNome(String token) {
        return (String) extractClaims(token).get("nome");
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, String ra) {
        final String extractedRa = extractRa(token);
        return (extractedRa.equals(ra) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
