package com.server.global.security.unit;

import com.server.global.config.security.jwt.JwtProvider;
import com.server.global.config.security.jwt.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class JwtProviderUnitTest {

    private JwtProvider jwtProvider;

    private String testSecretKey = "testsecrettestsecrettestsecrettestsecrettestsecrettestsecrettestsecrettestsecrettestsecret";

    private Key key;

    @BeforeEach
    void setUp() throws Exception {
        jwtProvider = new JwtProvider();

        key = new SecretKeySpec(
                testSecretKey.getBytes(),
                SignatureAlgorithm.HS256.getJcaName()
        );


        // private 필드에 값 주입
        setField(jwtProvider, "jwtSecretKey", testSecretKey);
        setField(jwtProvider, "jwtAccessExpiration", 3600000L); // 1 hour
        setField(jwtProvider, "jwtRefreshExpiration", 86400000L); // 24 hours
        setField(jwtProvider, "key", key); // key 초기화
        setField(jwtProvider, "parser", Jwts.parserBuilder()
                .setSigningKey(key)
                .build()); // parser 초기화

    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName); // private 필드 접근
        field.setAccessible(true);
        field.set(target, value);
    }

    // 토큰 검증 테스트
    @Test
    @DisplayName("JWT Claims 파싱 테스트")
    void parseClaimsTest() {
        int userId = 1;

        // 직접 JWT 생성
        String token = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // JwtProvider를 사용하여 Claims 파싱
        Claims claims = jwtProvider.parseClaims(token);

        // 검증
        assertEquals(String.valueOf(userId), claims.getSubject());
    }

    // 토큰 생성 테스트
    @Test
    @DisplayName("JWT 토큰 생성 테스트")
    void generateTokenTest() {
        String memberId = "1";
        String noneMemberId = "none";

        String token = jwtProvider.generateToken(memberId, TokenType.ACCESS_TOKEN);
        Claims claims = jwtProvider.parseClaims(token);

        assertEquals(memberId, claims.getSubject());
        assertEquals(memberId, claims.get("memberId"));
        assertEquals(TokenType.ACCESS_TOKEN.getValue(), claims.get("type"));
        assertNotEquals(noneMemberId, claims.getSubject());
    }


}
