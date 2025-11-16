package com.server.application.usecase.proposal.vote;

public interface VoteProposalUseCase {
    VoteProposalResult execute(VoteProposalCommand command);
}
