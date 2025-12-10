package com.server.presentation.proposal.proposal;

import com.server.application.usecase.proposal.proposal.ProposalListItemResult;
import com.server.domain.entity.proposal.ProposalStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "제안 목록 항목 응답")
public record ProposalListItemResponse(
        @Schema(description = "제안 아이디", example = "11") Long id,
        @Schema(description = "제안 제목", example = "급식 개선 요청") String title,
        @Schema(description = "제안 본문", example = "학생들을 위해 급식을 개선해주세요...") String body,
        @Schema(description = "상태", example = "COLLECTING") ProposalStatus status,
        @Schema(description = "조회수", example = "123") Long viewCount,
        @Schema(description = "해시태그 목록") List<String> hashtags,
        @Schema(description = "토픽 아이디") Long topicId,
        @Schema(description = "토픽 이름") String topicName,
        @Schema(description = "마감일", example = "2025-12-31") LocalDate dueDate,
        @Schema(description = "찬성 서명 수", example = "52") Long agreeSignatureCount,
        @Schema(description = "반대 서명 수", example = "4") Long disagreeSignatureCount,
        @Schema(description = "찬성 투표 수", example = "40") Long agreeVoteCount,
        @Schema(description = "반대 투표 수", example = "8") Long disagreeVoteCount,
        @Schema(description = "생성일시") LocalDateTime createdAt
) {
    public ProposalListItemResponse(ProposalListItemResult r) {
        this(
                r.id(),
                r.title(),
                r.body(),
                r.status(),
                r.viewCount(),
                r.hashtags(),
                r.topicId(),
                r.topicName(),
                r.dueDate(),
                r.agreeSignatureCount(),
                r.disagreeSignatureCount(),
                r.agreeVoteCount(),
                r.disagreeVoteCount(),
                r.createdAt()
        );
    }
}

