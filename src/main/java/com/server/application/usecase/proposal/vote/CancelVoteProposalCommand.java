package com.server.application.usecase.proposal.vote;

public record CancelVoteProposalCommand(
        Long userId,
        Long proposalId
) {}
