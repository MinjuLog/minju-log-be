package com.server.presentation.proposal.proposal;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "제안 생성 결과")
public record CreateProposalResponse(
        @Schema(description = "제안 아이디", example = "11") Long proposalId,
        @Schema(description = "제목", example = "급식 개선 요청") String title,
        @Schema(description = "본문", example = "급식 맛이 너무 없음") String body,
        @Schema(description = "상태", example = "COLLECTING") String status,
        @Schema(description = "해시태그 목록") List<String> hashtags,
        @Schema(description = "마감일(yyyy-MM-dd)", example = "2025-12-31") LocalDate dueDate,
        @Schema(description = "생성일시", example = "2025-11-16T01:00:00") LocalDateTime createdAt
) {}

