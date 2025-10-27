package com.server.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.global.common.base.BaseResponse;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인증은 되었으나 권한이 없는 사용자가 보호된 리소스에 접근하려 할 때 호출되는 핸들러
// 예외 응답을 위해 핸들러로 구현
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("Access denied: {}", accessDeniedException.getMessage());

        var errorCode = GlobalErrorStatus._FORBIDDEN; // ex) 403 Forbidden

        BaseResponse<String> errorResponse = BaseResponse.onFailure(
                errorCode.getCode().getCode(),
                errorCode.getMessage(),
                request.getRequestURI()
        );

        // 직접 응답 작성
        // Spring MVC 밖이기 때문에 @ControllerAdvice가 작동하지 않음
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
