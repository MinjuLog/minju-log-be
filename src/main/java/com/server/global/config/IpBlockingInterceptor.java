package com.server.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class IpBlockingInterceptor implements HandlerInterceptor {

    /**
     * 이 인터셉터는 단순히 요청을 통과시킵니다.
     * 실제 IP 차단 검사는 IpBlockingAspect에서 수행합니다.
     * 요청 바디가 ContentCachingRequestWrapper로 감싸져 있기 때문에
     * AOP에서 CreateProposalRequest 객체를 직접 검사할 수 있습니다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }
}
