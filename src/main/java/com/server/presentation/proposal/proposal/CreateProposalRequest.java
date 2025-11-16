package com.server.presentation.proposal.proposal;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CreateProposalRequest(

        @Schema(description = "사용자 아이디", example = "1")
        Long userId,

        @Schema(description = "제목", example = "급식 개선 요청")
        String title,

        @Schema(description = "본문", example = "급식 맛이 너무 없음")
        String body,

        @Schema(description = "해시태그 목록", example = "[\"#급식\", \"#개선\"]")
        List<String> hashtags,

        @Schema(description = "마감일(yyyy-MM-dd)", example = "2025-12-31")
        String dueDate // yyyy-MM-dd
) {}

