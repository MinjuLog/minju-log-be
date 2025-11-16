package com.server.presentation.proposal.signature;

import com.server.domain.entity.proposal.ProposalSignatureType;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignProposalRequest(
        @Schema(description = "사용자 아이디", example = "1")
        Long userId,
        @Schema(description = "서명 타입", example = "AGREE")
        ProposalSignatureType signatureType, // "AGREE" or "DISAGREE"
        @Schema(description = "서명 내용", example = "찬성합니다.")
        String content
) {}
