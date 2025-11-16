package com.server.presentation.proposal.signature;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "서명(서명 등록) 응답")
public record SignProposalResponse(
        @Schema(description = "서명 아이디", example = "33") Long signatureId,
        @Schema(description = "서명 타입", example = "AGREE") String signatureType,
        @Schema(description = "서명 내용") String content,
        @Schema(description = "생성일시") LocalDateTime createdAt
) {}