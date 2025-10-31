package fpt.com.capstone.util;

import fpt.com.capstone.model.Account;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpirationMs;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpirationMs;

    private final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Get signing key from secret
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate Access Token
    public String generateAccessToken(Account account) {
        return generateToken(account, jwtExpirationMs);
    }

    // Generate Refresh Token
    public String generateRefreshToken(Account account) {
        return generateToken(account, refreshTokenExpirationMs);
    }

    private String generateToken(Account account, Long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", account.getRole().name());
        claims.put("email", account.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(account.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Get Claims from Token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Get User ID from Token
    public Integer getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return Integer.parseInt(claims.getSubject());
    }

    // Get Email from Token
    public String getEmailFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("email", String.class);
    }

    // Get Role from Token
    public String getRoleFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    // Validate Token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // Get Expiration Time
    public Long getExpirationTime() {
        return jwtExpirationMs;
    }
}
