package com.server.presentation.proposal.vote;

public record VoteProposalRequest(
        Long userId,
        String voteType // "AGREE" or "DISAGREE"
) {}
