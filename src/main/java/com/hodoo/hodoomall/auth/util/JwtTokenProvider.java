package com.hodoo.hodoomall.auth.util;

import com.hodoo.hodoomall.auth.model.dao.RefreshTokenRepository;
import com.hodoo.hodoomall.auth.model.dto.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final long accessTokenExpTime = Duration.ofMinutes(1).toMillis();
    private final long refreshTokenValidTime = Duration.ofMinutes(2).toMillis();

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String userId, String level) {
        Claims claims = Jwts.claims().setSubject(userId);
        String role;
        if (level.equals("admin")) {
            role = "ROLE_ADMIN";
        } else {
            role = "ROLE_USER";
        }
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenExpTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(this.getSigningKey())
                .compact();
    }

    public String createRefreshToken(String userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidTime);

        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(this.getSigningKey())
                .compact();
        refreshTokenRepository.save(new RefreshToken(userId, refreshToken));

        return refreshToken;
    }

    // JWT 토큰의 유효성 검사 메서드
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(this.getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // JWT에서 사용자 ID 추출
    public String getUserId(String token) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(this.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            return claims.getSubject();
        }

    }

    // JWT에서 사용자 권한 추출
    public String getRole(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(this.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class);

        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            return claims.getSubject();
        }

    }


}
