package com.server.application.usecase.proposal.signature;

import com.server.domain.entity.proposal.ProposalSignatureType;

public record SignProposalCommand(
        Long userId,
        Long proposalId,
        ProposalSignatureType signatureType,
        String content
) {}
