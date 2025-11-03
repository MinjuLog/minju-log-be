package com.server.global.config.security.jwt;

import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import com.server.global.config.security.model.CustomPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
// 매 요청마다 JWT를 검증·파싱해 SecurityContext에 인증을 복원하는 필터
// (세션 대체 느낌, 예외 응답은 JwtExceptionFilter가 수행)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtValidator jwtValidator;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Authorization 헤더에서 토큰 추출
        String token = resolveToken(request);

        // 토큰이 존재하면 검증 후 인증객체 세팅
        if (token != null) {
            // JWT 유효성 검증 (예외 발생 시 JwtExceptionFilter가 처리)
            jwtValidator.validate(token);

            // 토큰이 유효하면 Claims(페이로드) 추출
            Claims claims = jwtProvider.parseClaims(token);
            long memberId;
            try{
                // Claims에서 사용자 정보 추출, 사용자 정보가 없으면 예외 처리
                if(claims.get("memberId") == null) throw new RestApiException(GlobalErrorStatus._NOT_FOUND_USER_INFO);

                memberId = Long.parseLong(claims.get("memberId").toString());
            } catch (NumberFormatException e){
                throw new RestApiException(GlobalErrorStatus._NUMBER_FORMAT_EXCEPTION);
            }

            // DB에서 사용자 정보 조회 (Spring Security 표준 UserDetails)
            CustomPrincipal userDetails = new CustomPrincipal(memberId);

            // 인증 객체 생성 (비밀번호 null, 권한 userDetails에서 가져옴)
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // SecurityContextHolder에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("JWT 인증 성공 - memberId: {}", memberId);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 Bearer 토큰을 추출한다.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
