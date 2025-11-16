package com.server.presentation.converter;

import com.server.application.usecase.proposal.vote.VoteProposalCommand;
import com.server.presentation.proposal.vote.VoteProposalRequest;
import com.server.domain.entity.proposal.ProposalVoteType;

public class VoteProposalConverter {

    private VoteProposalConverter() {}

    public static VoteProposalCommand toCommand(Long proposalId, VoteProposalRequest request) {
        return new VoteProposalCommand(
                request.userId(),
                proposalId,
                request.voteType()
        );
    }
}
