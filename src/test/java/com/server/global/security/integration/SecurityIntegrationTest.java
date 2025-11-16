/*
package com.server.global.security.integration;


import com.server.domain.test.repository.TestRepository;
import com.server.domain.test.service.TestService;
import com.server.global.config.security.jwt.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    private String validAccessToken;

    @MockBean
    private TestRepository testRepository;

    @MockBean
    private TestService testService;

    @MockBean
    private UserDetailsService userDetailsService;



    @BeforeEach
    void setUp() {
        // 실제 JwtProvider를 이용해 토큰 생성 (만료기간, 시그니처 등 모두 실제 설정 사용)
        validAccessToken = jwtProvider.generateToken("123", TokenType.ACCESS_TOKEN);
    }

    @Test
    @DisplayName("Bearer 토큰 없이 요청하면 401 반환")
    void noToken_unauthorized() throws Exception {
        mockMvc.perform(get("/tests/secure"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("유효한 JWT로 인증 필터 통과 → 200 OK")
    void validJwtToken_allowsAccess() throws Exception {
        mockMvc.perform(get("/tests/secure")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("만료된 JWT로 접근 시 401 반환")
    void expiredJwtToken_returnsUnauthorized() throws Exception {
        // 🔹 만료된 토큰 강제로 만들려면, 만료시간 짧게 설정한 테스트 프로퍼티나
        // JwtProvider 테스트용 메서드를 따로 두는 게 안전함
        String expiredToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
                + " (만료된 JWT 예시)";
        mockMvc.perform(get("/tests/secure")
                        .header("Authorization", expiredToken))
                .andExpect(status().isUnauthorized());
    }
}
*/
