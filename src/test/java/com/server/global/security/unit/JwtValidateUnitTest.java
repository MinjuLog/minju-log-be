package com.server.global.security.unit;

import com.server.global.common.exception.RestApiException;
import com.server.global.config.security.jwt.JwtProvider;
import com.server.global.config.security.jwt.JwtValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.server.global.common.exception.code.status.GlobalErrorStatus.*;
import static org.junit.jupiter.api.Assertions.*;

// 내가 정의한 예외를 던지는 지 검증
public class JwtValidateUnitTest {

    private JwtValidator jwtValidator;

    // 가짜 클래스
    // 예외만 던지는 클래스
    static class FakeJwtProvider extends JwtProvider {
        private final RuntimeException ex;
        FakeJwtProvider(RuntimeException ex) { this.ex = ex; }
        @Override protected void init() { /* 초기화 생략 */ }
        @Override public Claims parseClaims(String token) {
            if (ex != null) throw ex;
            return null;
        }
    }

    @Test
    @DisplayName("토큰 Null 검증 테스트")
    void validateNullTokenTest() {
        jwtValidator = new JwtValidator(new FakeJwtProvider(null));

        // 예외를 던지는 지
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            jwtValidator.validate(null);
        });
        // 예외가 내가 기대한 예외인지
        assertEquals(_EMPTY_TOKEN.getCode().getCode(), exception.getErrorCode().getCode());

    }

    @Test
    @DisplayName("만료된 토큰 검증 테스트")
    void validateExpiredTokenTest() {
        jwtValidator = new JwtValidator(new FakeJwtProvider(new ExpiredJwtException(null, null, "Token expired")));

        RestApiException exception = assertThrows(RestApiException.class, () -> {
            jwtValidator.validate("expiredToken");
        });

        assertEquals(_EXPIRED_TOKEN.getCode().getCode(), exception.getErrorCode().getCode());
    }

    @Test
    @DisplayName("잘못된 토큰 형식 검증 테스트")
    void validateMalformedTokenTest() {
        jwtValidator = new JwtValidator(new FakeJwtProvider(new MalformedJwtException("Malformed token")));

        RestApiException exception = assertThrows(RestApiException.class, () -> {
            jwtValidator.validate("malformedToken");
        });

        assertEquals(_MALFORMED_TOKEN.getCode().getCode(), exception.getErrorCode().getCode());
    }

    @Test
    @DisplayName("잘못된 서명 토큰 검증 테스트")
    void validateSignatureInvalidTokenTest() {
        jwtValidator = new JwtValidator(new FakeJwtProvider(new SecurityException("Invalid signature")));

        RestApiException exception = assertThrows(RestApiException.class, () -> {
            jwtValidator.validate("signatureInvalidToken");
        });

        assertEquals(_SIGNATURE_INVALID.getCode().getCode(), exception.getErrorCode().getCode());
    }

    @Test
    @DisplayName("기타 JWT 처리 오류 검증 테스트")
    void validateOtherJwtExceptionTest() {
        jwtValidator = new JwtValidator(new FakeJwtProvider(new JwtException("Other JWT error")));

        RestApiException exception = assertThrows(RestApiException.class, () -> {
            jwtValidator.validate("otherJwtErrorToken");
        });

        assertEquals(_TOKEN_VALIDATION_FAILED.getCode().getCode(), exception.getErrorCode().getCode());
    }


    @Test
    @DisplayName("유효한 토큰 검증 테스트")
    void validateValidTokenTest() {
        // 유효한 토큰의 경우 예외가 발생하지 않아야 함
        jwtValidator = new JwtValidator(new FakeJwtProvider(null));

        // 예외를 던지지 않는지 확인
        assertDoesNotThrow(() -> {
            jwtValidator.validate("validToken");
        });
    }
}
