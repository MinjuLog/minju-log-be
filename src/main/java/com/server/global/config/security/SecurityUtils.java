package com.server.global.config.security;

import com.server.global.common.exception.RestApiException;
import com.server.global.common.exception.code.status.GlobalErrorStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나, "anonymousUser"일 경우 null 반환
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            try{
                return Long.valueOf(((UserDetails) principal).getUsername());
            } catch (NumberFormatException e){
                throw new RestApiException(GlobalErrorStatus._NUMBER_FORMAT_EXCEPTION);
            }

        } else {
            return null;
        }
    }
}
