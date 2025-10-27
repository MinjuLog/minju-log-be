package com.server.global.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.global.common.base.BaseResponse;
import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.BaseCodeDto;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
// JWT 처리 중 발생하는 예외를 잡아내서 json으로 던지는 필터 (에러 던지기)
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RestApiException e) {
            log.warn("JWT 예외 발생: {}", e.getErrorCode().getMessage());
            setErrorResponse(response, e.getErrorCode());
        } catch (Exception e) {
            log.error("JWT 필터 내부 에러", e);
            setErrorResponse(response, GlobalErrorStatus._INTERNAL_SERVER_ERROR.getCode());
        }
    }

    private void setErrorResponse(HttpServletResponse response, BaseCodeDto errorCode) throws IOException {
        BaseResponse<Object> baseResponse = BaseResponse.onFailure(
                errorCode.getCode(),
                errorCode.getMessage(),
                null
        );

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(response.getOutputStream(), baseResponse);
    }
}
