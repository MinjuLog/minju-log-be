package com.server.presentation.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "랜덤 생성된 유저 정보")
public record CreateRandomUserResponse(
        @Schema(description = "유저 아이디", example = "1") Long userId,
        @Schema(description = "닉네임", example = "빠른-너구리-23") String nickname,
        @Schema(description = "생성일시", example = "2025-01-01T13:00:00") LocalDateTime createdAt
) {}