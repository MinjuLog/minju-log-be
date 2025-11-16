package com.server.application.usecase.proposal.vote;

import com.server.domain.entity.proposal.ProposalVoteType;

public record VoteProposalCommand(
        Long userId,
        Long proposalId,
        ProposalVoteType voteType
) {}
