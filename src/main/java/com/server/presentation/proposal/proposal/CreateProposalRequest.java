package com.server.presentation.proposal.proposal;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CreateProposalRequest(

        @Schema(description = "사용자 아이디", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long userId,

        @Schema(description = "제목", example = "급식 개선 요청", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "본문", example = "급식 맛이 너무 없음", requiredMode = Schema.RequiredMode.REQUIRED)
        String body,

        @Schema(description = "토픽 ID (선택사항)", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Long topicId,

        @Schema(description = "해시태그 목록", example = "[\"#급식\", \"#개선\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<String> hashtags,

        @Schema(description = "마감일(yyyy-MM-dd, 선택사항)", example = "2025-12-31", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String dueDate // yyyy-MM-dd
) {}

