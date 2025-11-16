package com.server.application.usecase.proposal.signature;

import java.time.LocalDateTime;

public record SignatureListItemResult(
        Long signatureId,
        Long userId,
        String nickname,
        String signatureType,
        String content,
        LocalDateTime createdAt
) {}
