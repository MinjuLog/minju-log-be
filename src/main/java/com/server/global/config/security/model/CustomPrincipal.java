package com.server.global.config.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


/**
 * JWT 기반 인증 환경에서 사용하는 최소 UserDetails 구현체.
 * - memberId만 포함
 * - 매 요청마다 JWT에서 복원됨 (스프링 빈 아님)
 */
@Getter
@AllArgsConstructor
public class CustomPrincipal implements UserDetails {
    private Long memberId;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 사용 안 하면 빈 컬렉션 반환
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        // 소셜 로그인만 지원하므로 비밀번호는 필요 없음
        return "";
    }

    @Override
    public String getUsername() {
        // member의 id를 username으로 사용
        return memberId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
