package com.server.global.config.security.jwt;

import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
// JWT 토큰의 유효성을 검증하는 클래스
public class JwtValidator {

    private final JwtProvider jwtProvider;

    public void validate(String token) {
        if (token == null || token.isEmpty()) {
            throw new RestApiException(GlobalErrorStatus._TOKEN_VALIDATION_FAILED);
        }

        try {
            jwtProvider.parseClaims(token); // 파싱 자체에서 JWT 예외 발생
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("만료된 토큰: {}", e.getMessage());
            throw new RestApiException(GlobalErrorStatus._EXPIRED_TOKEN);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.warn("잘못된 토큰 형식: {}", e.getMessage());
            throw new RestApiException(GlobalErrorStatus._MALFORMED_TOKEN);
        } catch (io.jsonwebtoken.SignatureException e) {
            log.warn("잘못된 서명: {}", e.getMessage());
            throw new RestApiException(GlobalErrorStatus._SIGNATURE_INVALID);
        } catch (io.jsonwebtoken.JwtException e) {
            log.warn("JWT 처리 오류: {}", e.getMessage());
            throw new RestApiException(GlobalErrorStatus._TOKEN_VALIDATION_FAILED);
        }
    }
}

