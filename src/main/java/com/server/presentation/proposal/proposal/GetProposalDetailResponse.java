package com.server.presentation.proposal.proposal;

import com.server.application.usecase.proposal.proposal.GetProposalDetailResult;
import com.server.application.usecase.proposal.signature.SignatureListItemResult;
import com.server.presentation.proposal.signature.SignatureListItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "제안 상세 응답")
public record GetProposalDetailResponse(
    @Schema(description = "제안 아이디", example = "11") Long id,
    @Schema(description = "제목", example = "급식 개선 요청") String title,
    @Schema(description = "본문") String body,
    @Schema(description = "상태", example = "COLLECTING") String status,
    @Schema(description = "조회수", example = "123") Long viewCount,
    @Schema(description = "해시태그 목록") List<String> hashtags,
    @Schema(description = "토픽 아이디") Long topicId,
    @Schema(description = "토픽 이름") String topicName,
    @Schema(description = "마감일", example = "2025-12-31") LocalDate dueDate,
    @Schema(description = "찬성 서명 수", example = "52") long agreeSignatureCount,
    @Schema(description = "반대 서명 수", example = "4") long disagreeSignatureCount,
    @Schema(description = "찬성 투표 수", example = "40") long agreeVoteCount,
    @Schema(description = "반대 투표 수", example = "8") long disagreeVoteCount,
    @Schema(description = "내 서명 정보") MySignatureResponse mySignature,
    @Schema(description = "내 투표 정보") MyVoteResponse myVote,
    @Schema(description = "최근 서명 목록") List<SignatureListItemResponse> recentSignatures,
    @Schema(description = "생성일시") LocalDateTime createdAt
) {
    @Schema(description = "내 서명 정보")
    public static record MySignatureResponse(@Schema(description = "서명 여부") boolean didSign, @Schema(description = "서명 타입") String type, @Schema(description = "내용") String content) {}

    @Schema(description = "내 투표 정보")
    public static record MyVoteResponse(@Schema(description = "투표 여부") boolean didVote, @Schema(description = "투표 타입") String type) {}

    public GetProposalDetailResponse(GetProposalDetailResult r) {
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
        new MySignatureResponse(r.mySignature().didSign(), r.mySignature().type(), r.mySignature().content()),
        new MyVoteResponse(r.myVote().didVote(), r.myVote().type()),
        r.recentSignatures().stream().map(s -> new SignatureListItemResponse(
            s.signatureId(), s.userId(), s.nickname(), s.signatureType(), s.content(), s.createdAt()
        )).toList(),
        r.createdAt()
    );
    }
}

