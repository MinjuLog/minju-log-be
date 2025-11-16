package com.server.application.usecase.proposal.signature;

import java.time.LocalDateTime;

public record SignProposalResult(
        Long signatureId,
        String signatureType,
        String content,
        LocalDateTime createdAt
) {}
