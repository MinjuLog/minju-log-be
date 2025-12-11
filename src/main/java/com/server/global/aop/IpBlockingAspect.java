package com.server.global.aop;

import com.server.global.util.IpBlockingUtil;
import com.server.presentation.proposal.proposal.CreateProposalRequest;
import com.server.presentation.proposal.signature.SignProposalRequest;
import com.server.presentation.proposal.vote.VoteProposalRequest;
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
        checkIpBlocking(joinPoint);
    }

    /**
     * POST /api/proposals/{proposalId}/signatures 요청 전에 IP 차단 확인
     */
    @Before("execution(* com.server.presentation.proposal.signature.ProposalSignatureController.sign(..))")
    public void checkIpBlockingBeforeSign(JoinPoint joinPoint) throws Exception {
        log.info("[AOP] ProposalSignatureController.sign() 호출 전 IP 차단 검사");
        checkIpBlocking(joinPoint);
    }

    /**
     * POST /api/proposals/{proposalId}/votes 요청 전에 IP 차단 확인
     */
    @Before("execution(* com.server.presentation.proposal.vote.ProposalVoteController.vote(..))")
    public void checkIpBlockingBeforeVote(JoinPoint joinPoint) throws Exception {
        log.info("[AOP] ProposalVoteController.vote() 호출 전 IP 차단 검사");
        checkIpBlocking(joinPoint);
    }

    /**
     * 실제 IP 차단 검사 로직
     */
    private void checkIpBlocking(JoinPoint joinPoint) throws Exception {
        // HttpServletRequest 얻기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("ServletRequestAttributes를 찾을 수 없음");
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        
        // 메서드 인자에서 요청 객체 추출
        Object[] args = joinPoint.getArgs();
        Object requestObject = null;
        
        for (Object arg : args) {
            if (arg instanceof CreateProposalRequest 
                    || arg instanceof SignProposalRequest
                    || arg instanceof VoteProposalRequest) {
                requestObject = arg;
                break;
            }
        }

        if (requestObject == null) {
            log.warn("요청 객체를 찾을 수 없음");
            return;
        }

        Long userId = extractUserId(requestObject);
        log.info("[AOP] userId={}", userId);

        // 1. 악성 유저면 IP 차단 (차단되면 예외 발생)
        if (userId != null) {
            log.info("[AOP] userId={}에 대해 악성 유저 검사", userId);
            ipBlockingUtil.blockUserIpIfMalicious(userId, request);
        }

        // 2. 악성 유저가 아니면 현재 IP가 차단되었는지 확인
        log.info("[AOP] IP 차단 여부 확인");
        ipBlockingUtil.validateRequestIpNotBlocked(request);

        log.info("[AOP] IP 차단 검사 완료 - 컨트롤러 메서드 실행");
    }

    /**
     * 요청 객체에서 userId 추출
     */
    private Long extractUserId(Object requestObject) {
        if (requestObject instanceof CreateProposalRequest) {
            return ((CreateProposalRequest) requestObject).userId();
        } else if (requestObject instanceof SignProposalRequest) {
            return ((SignProposalRequest) requestObject).userId();
        } else if (requestObject instanceof VoteProposalRequest) {
            return ((VoteProposalRequest) requestObject).userId();
        }
        return null;
    }
}
