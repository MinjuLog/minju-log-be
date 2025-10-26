//package com.server.global.ratelimit;
//
//import com.server.global.config.ratelimit.RateLimitProperties;
//import io.github.bucket4j.Bucket;
//import io.github.bucket4j.BucketConfiguration;
//import io.github.bucket4j.ConsumptionProbe;
//import io.github.bucket4j.distributed.proxy.ProxyManager;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.time.Duration;
//
//@Slf4j
////@Component
//@RequiredArgsConstructor
//public class RateLimitFilter extends OncePerRequestFilter {
//    private final ProxyManager<String> proxyManager;
//    private final BucketConfiguration defaultBucketConfiguration;
//    private final ClientKeyResolver keyResolver;
//    private final RateLimitProperties props;
//
//    private final AntPathMatcher pathMatcher = new AntPathMatcher();
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        if (!props.isEnabled()) return true;
//
//        String path = request.getRequestURI();
//        // 화이트리스트 경로는 제외
//        return props.getWhitelistPaths().stream().anyMatch(p -> pathMatcher.match(p, path));
//    }
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain chain
//    ) throws ServletException, IOException {
//
//        final String key;
//        try {
//            key = keyResolver.resolve(request); // API Key → JWT memberId → IP
//        } catch (Exception e) {
//            // 키 해석 중 예외는 너무 공격적이니 fail-open
//            log.warn("[RateLimit] key resolve failed, pass-through. uri={}", request.getRequestURI(), e);
//            chain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            Bucket bucket = proxyManager.builder().build(key, () -> defaultBucketConfiguration);
//
//            ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
//            if (probe.isConsumed()) {
//                // 남은 토큰/정책 헤더 (클라이언트 UX 향상)
//                response.setHeader("X-RateLimit-Limit", props.getLimit() + ";window=" + props.getPeriod());
//                response.setHeader("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));
//                chain.doFilter(request, response);
//                return;
//            }
//
//            // 초과: 429 + Retry-After
//            long nanosToWait = probe.getNanosToWaitForRefill();
//            long secondsToWait = Math.max(1, Duration.ofNanos(nanosToWait).toSeconds());
//            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//            response.setHeader("Retry-After", String.valueOf(secondsToWait));
//
//            // 에러 바디(단순 문자열 또는 JSON으로 바꾸고 싶으면 GlobalExceptionHandler로 통일)
//            String message = "Too Many Requests";
//            if (!response.isCommitted()) {
//                response.setContentType("text/plain;charset=UTF-8");
//                response.getWriter().write(message);
//            }
//
//        } catch (Exception e) {
//            // Redis/네트워크 등 문제 시 서비스 전체가 막히지 않도록 fail-open
//            log.error("[RateLimit] error while checking limit, pass-through. key={}", safe(key), e);
//            chain.doFilter(request, response);
//        }
//    }
//
//    private String safe(String s) {
//        if (!StringUtils.hasText(s)) return "";
//        // 너무 긴 키를 로그에 그대로 찍지 않게 간략화
//        return s.length() > 120 ? s.substring(0, 120) + "..." : s;
//    }
//}
