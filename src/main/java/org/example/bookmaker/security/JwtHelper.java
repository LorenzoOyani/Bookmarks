package org.example.bookmaker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.example.bookmaker.configuration.JwtProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class JwtHelper {

    private final JwtProperties jwtProperties;

    // create and validate a token
    // this method returns a string token that needs to be passed to the Authorization header
    public String createToken(Map<String, Object> claims, String subject) {
        Date expirationDate = Date.from(Instant.ofEpochMilli(System.currentTimeMillis()
                + jwtProperties.getJwtExpiration() * 1000));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(getSignedKey())
                .compact();
    }


    public String extractUsername(String bearerToken) {
        return extractUserClaimsBody(bearerToken, Claims::getSubject);
    }

    public <T> T extractUserClaimsBody(String bearerToken, Function<Claims, T> claimsResolver) {
        Jws<Claims> claimsJws = extractClaims(bearerToken);
        return claimsResolver.apply(claimsJws.getPayload());
    }

    private Jws<Claims> extractClaims(String bearerToken) {
        return Jwts.parser()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(bearerToken);
    }

    private Key getSignedKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getJwtSecret());
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    // validate token by checking if the extracted username from bearer token is equals to the system userDetails
    public boolean validateToken(String bearerToken, UserDetails userDetails) {
        final String username = extractUsername(bearerToken);
        return username.equals(userDetails.getUsername()) && !tokenExpired(bearerToken);
    }

    private boolean tokenExpired(String bearerToken) {
        return extractExpiry(bearerToken).before(new Date());
    }

    private Date extractExpiry(String bearerToken) {
        return extractUserClaimsBody(bearerToken, Claims::getExpiration);
    }
}
