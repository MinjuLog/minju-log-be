package com.server.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.global.common.base.BaseResponse;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인증되지 않은 사용자가 보호된 리소스에 접근하려 할 때 호출되는 핸들러
// 예외 응답을 위해 핸들러로 구현
@Slf4j
@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        log.warn("Authentication failed: {}", authException.getMessage());

        // 기존 전역 예외 코드 구조 활용
        var errorCode = GlobalErrorStatus._UNAUTHORIZED; // ex) 401 Unauthorized

        BaseResponse<String> errorResponse = BaseResponse.onFailure(
                errorCode.getCode().getCode(),
                errorCode.getMessage(),
                request.getRequestURI()
        );

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}