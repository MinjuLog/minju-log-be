//package com.server.global.ratelimit;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
////@Component
//@RequiredArgsConstructor
//public class ClientKeyResolver { // clientKey 생성기
//
//    private final JwtProvider jwtProvider;
//
//    //API Key → JWT(memberId) → IP의 우선 순위 기반으로 키 생성
//    public String resolve(HttpServletRequest request) {
//        // API Key 헤더 우선
//        String apiKey = request.getHeader("X-Api-Key");
//        if (StringUtils.hasText(apiKey)) {
//            return "apiKey:" + apiKey;
//        }
//
//        // JWT Access Token 에서 memberId 추출
//        String token = jwtProvider.resolveToken(request);
//        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
//            Claims claims = jwtProvider.getClaims(token);
//            Object memberId = claims.get("memberId");
//            if (memberId != null) {
//                return "user:" + memberId;
//            }
//        }
//
//        // IP 기반 fallback
//        String ip = request.getHeader("X-Forwarded-For");
//        if (!StringUtils.hasText(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        return "ip:" + ip;
//    }
//
//}
