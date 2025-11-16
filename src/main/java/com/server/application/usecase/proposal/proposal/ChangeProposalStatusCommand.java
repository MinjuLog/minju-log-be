package com.server.application.usecase.proposal.proposal;

public record ChangeProposalStatusCommand(
        Long proposalId,
        String status
) {}
