package com.server.presentation.proposal.signature;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "서명 목록 항목 응답")
public record SignatureListItemResponse(
        @Schema(description = "서명 아이디", example = "33") Long signatureId,
        @Schema(description = "작성자 유저 아이디", example = "102") Long userId,
        @Schema(description = "작성자 닉네임", example = "minju123") String nickname,
        @Schema(description = "서명 타입", example = "AGREE") String signatureType,
        @Schema(description = "서명 내용") String content,
        @Schema(description = "생성일시") LocalDateTime createdAt
) {}
