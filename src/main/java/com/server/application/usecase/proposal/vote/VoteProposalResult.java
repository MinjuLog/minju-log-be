package com.server.application.usecase.proposal.vote;

public record VoteProposalResult(
        Long voteId,
        Long proposalId,
        Long userId,
        String voteType
) {}
