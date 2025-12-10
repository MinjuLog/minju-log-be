package com.server.global.aop;

import com.server.global.util.IpBlockingUtil;
import com.server.presentation.proposal.proposal.CreateProposalRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IpBlockingAspect {

    private final IpBlockingUtil ipBlockingUtil;

    /**
     * POST /api/proposals 요청 전에 userId를 검사하고 IP 차단 확인
     */
    @Before("execution(* com.server.presentation.proposal.proposal.ProposalController.create(..))")
    public void checkIpBlockingBeforeCreateProposal(JoinPoint joinPoint) throws Exception {
        log.info("[AOP] ProposalController.create() 호출 전 IP 차단 검사");

        // HttpServletRequest 얻기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("ServletRequestAttributes를 찾을 수 없음");
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        
        // 메서드 인자에서 CreateProposalRequest 추출
        Object[] args = joinPoint.getArgs();
        CreateProposalRequest createProposalRequest = null;
        
        for (Object arg : args) {
            if (arg instanceof CreateProposalRequest) {
                createProposalRequest = (CreateProposalRequest) arg;
                break;
            }
        }

        if (createProposalRequest == null) {
            log.warn("CreateProposalRequest를 찾을 수 없음");
            return;
        }

        Long userId = createProposalRequest.userId();
        log.info("[AOP] userId={}", userId);

        if (userId != null) {
            // 1. 악성 유저면 IP 차단
            log.info("[AOP] userId={}에 대해 악성 유저 검사", userId);
            ipBlockingUtil.blockUserIpIfMalicious(userId, request);
        }

        // 2. IP가 차단되었는지 확인
        log.info("[AOP] IP 차단 검사");
        ipBlockingUtil.validateRequestIpNotBlocked(request);

        log.info("[AOP] IP 차단 검사 완료 - 컨트롤러 메서드 실행");
    }
}
