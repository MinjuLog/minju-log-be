package com.server.presentation.proposal.signature;

public record SignProposalRequest(
        Long userId,
        String signatureType, // "AGREE" or "DISAGREE"
        String content
) {}
