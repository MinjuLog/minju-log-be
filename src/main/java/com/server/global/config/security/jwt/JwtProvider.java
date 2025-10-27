package com.server.global.config.security.jwt;

import com.server.global.common.exception.RestApiException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${jwt.accessExpiration}")
    private long jwtAccessExpiration;

    @Value("${jwt.refreshExpiration}")
    private long jwtRefreshExpiration;

    private Key key;

    private JwtParser parser;

    // 모든 의존성 주입이 끝난 뒤 한 번만 실행되는 초기화 로직 (@PostConstruct로 null 주입 문제 없이 안전하게 처리)
    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecretKey);
        this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());

        // parser 한번만 빌드
        this.parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    public String generateToken(String memberId, TokenType type) {
        Date now = new Date();
        Date expiration;

        // 토큰 타입에 따라 만료 시간 설정
        if (TokenType.ACCESS_TOKEN.equals(type)) { // 액세스 토큰
            expiration=calculateExpirationDate(now, jwtAccessExpiration);
        } else { // 리프레쉬 토큰
            expiration=calculateExpirationDate(now, jwtRefreshExpiration);
        }

        Claims claims = Jwts.claims().setSubject(memberId); // JWT payload 에 저장되는 정보단위
        claims.put("memberId", memberId);
        claims.put("type", type.getValue());

        return Jwts.builder()
                    .setSubject(memberId)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
    }


    public Claims parseClaims(String token) throws JwtException {
        // JWT의 핵심 파싱 로직
        return parser
                .parseClaimsJws(token)
                .getBody();
    }

    // 만료시간 계산
    private Date calculateExpirationDate(Date createdDate, long jwtExpiration) {
        return new Date(createdDate.getTime() + jwtExpiration);
    }
}
