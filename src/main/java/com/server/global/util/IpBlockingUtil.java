package com.server.global.util;

import com.server.domain.entity.user.BlockedIp;
import com.server.domain.entity.user.User;
import com.server.domain.repository.user.BlockedIpRepository;
import com.server.domain.repository.user.UserRepository;
import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IpBlockingUtil {

    private final BlockedIpRepository blockedIpRepository;
    private final UserRepository userRepository;

    // 악성 유저 ID 목록
    private static final List<Long> MALICIOUS_USER_IDS = Arrays.asList(81L, 48L, 49L, 63L, 92L, 94L, 78L, 75L, 1L, 133L, 83L);

    /**
     * HTTP 요청에서 클라이언트 IP 주소 추출
     */
    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // X-Forwarded-For는 콤마로 구분된 여러 IP를 포함할 수 있으므로 첫 번째만 사용
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }

    /**
     * IP가 차단되었는지 확인
     */
    public void validateIpNotBlocked(String ipAddress) {
        if (blockedIpRepository.existsByIpAddress(ipAddress)) {
            throw new RestApiException(GlobalErrorStatus._FORBIDDEN);
        }
    }

    /**
     * 요청의 IP가 차단되었는지 확인
     */
    public void validateRequestIpNotBlocked(HttpServletRequest request) {
        String clientIp = getClientIp(request);
        validateIpNotBlocked(clientIp);
    }

    /**
     * 특정 유저 ID가 악성 유저면 IP를 차단 테이블에 자동 저장
     */
    public void blockUserIpIfMalicious(Long userId, HttpServletRequest request) {
        if (MALICIOUS_USER_IDS.contains(userId)) {
            String clientIp = getClientIp(request);
            
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                // 유저의 IP 주소가 없으면 현재 요청의 IP를 저장
                if (user.getIpAddress() == null || user.getIpAddress().isBlank()) {
                    user.setIpAddress(clientIp);
                    userRepository.save(user);
                } else {
                    clientIp = user.getIpAddress();
                }
                
                // 이미 차단된 IP가 아니면 차단 테이블에 추가
                if (!blockedIpRepository.existsByIpAddress(clientIp)) {
                    BlockedIp blockedIp = BlockedIp.of(user, clientIp, "악성 사용자 활동 감지");
                    blockedIpRepository.save(blockedIp);
                }
            }
            
            // IP 차단
            throw new RestApiException(GlobalErrorStatus._FORBIDDEN);
        }
    }

    /**
     * 유저 생성/로그인 시 IP 저장
     */
    public void saveUserIp(User user, String ipAddress) {
        user.setIpAddress(ipAddress);
        userRepository.save(user);
    }
}
