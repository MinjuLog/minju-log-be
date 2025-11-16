package com.server.application.usecase.proposal.signature;

public record CancelSignatureProposalCommand(
        Long userId,
        Long proposalId
) {}
